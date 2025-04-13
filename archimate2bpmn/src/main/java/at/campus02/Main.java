package at.campus02;

import java.io.File;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.javatuples.Pair;

import java.util.Map;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.DataInputRefs;
import org.camunda.bpm.model.bpmn.impl.instance.DataOutputRefs;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.Collaboration;
import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.DataInput;
import org.camunda.bpm.model.bpmn.instance.DataOutput;
import org.camunda.bpm.model.bpmn.instance.DataObject;
import org.camunda.bpm.model.bpmn.instance.DataObjectReference;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.Documentation;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.ErrorEventDefinition;
import org.camunda.bpm.model.bpmn.instance.EventBasedGateway;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InputSet;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.IntermediateThrowEvent;
import org.camunda.bpm.model.bpmn.instance.IoSpecification;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.LaneSet;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.bpm.model.bpmn.instance.OutputSet;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.camunda.bpm.model.bpmn.instance.TerminateEventDefinition;
import org.camunda.bpm.model.bpmn.instance.TimeCycle;
import org.camunda.bpm.model.bpmn.instance.TimeDate;
import org.camunda.bpm.model.bpmn.instance.TimeDuration;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaConnector;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaConnectorId;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaScript;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.bpmn.instance.di.Shape;
import org.camunda.bpm.model.bpmn.instance.di.Waypoint;
import org.w3c.dom.Document;

/**
 * Transform ArchiMate Model XML to BPMN Model XML
 * 
 * @param args [ArchiMateModel.xml]
 *
 */

public class Main {

    public static void main(String[] args) throws Exception {
        // Check for correct number of Parameters
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }

        // Load the ArchiMate Model file
        Document archiMateModel = ArchiMateXMLLoader.getXMLDocument(args[0]);
        String archiMateModelID = ArchiMateXMLLoader.removePrefix(archiMateModel.getDocumentElement().getAttribute("identifier"));

        // Mappings between ArchiMate and BPMN
        List<Pair<String,String>> mappings = new ArrayList<>();
        

        // Create an empty BPMN Model
        BpmnModelInstance bpmnmodel = Bpmn.createEmptyModel();


        // Create the root definitions element
        Definitions definitions = bpmnmodel.newInstance(Definitions.class);
            definitions.setTargetNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL");
            definitions.setExporter("ArchiMate2BPMN");
            definitions.setExporterVersion("1.0.0");
            definitions.setId("definitions_" + archiMateModelID);
            definitions.setAttributeValueNs("http://www.w3.org/2000/xmlns/", "xmlns:camunda", "http://camunda.org/schema/1.0/bpmn");
            bpmnmodel.setDefinitions(definitions);
            

        // Create BPMNDiagram and BPMNPlane
        BpmnDiagram diagram = bpmnmodel.newInstance(BpmnDiagram.class);
            diagram.setId("BPMNDiagram_" + archiMateModelID);
            definitions.addChildElement(diagram);

        BpmnPlane plane = bpmnmodel.newInstance(BpmnPlane.class);
            plane.setId("Plane_" + archiMateModelID);
            diagram.addChildElement(plane);


        // Check if a collaboration and participants must be created
        List<String> pools = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "Location");
        List<Pair<Participant, Element>> participants = new ArrayList<>();
        List<Pair<Process, Element>> processes = new ArrayList<>();
        List<Pair<Process, IoSpecification>> processToIoSpecification = new ArrayList<>();

        if (pools.size()>0) {
            // Location(s) / Pool(s) are given, therefore create collaboration, processes and participants
            Collaboration collaboration = bpmnmodel.newInstance(Collaboration.class);
                collaboration.setId("Collaboration_" + archiMateModelID);
                definitions.addChildElement(collaboration);

                // Add collaboration to plane
                BPMNElementCreator.addCollaborationToPlane(bpmnmodel, collaboration);
            
            for (int i = 0; i < pools.size(); i++) {
                Element pool = ArchiMateXMLLoader.findElementById(archiMateModel, pools.get(i));
                Process process = bpmnmodel.newInstance(Process.class);
                    process.setId(("Process_" + ArchiMateXMLLoader.removePrefix(pools.get(i))).toUpperCase());
                    process.setName(ArchiMateXMLLoader.getNameFromElement(pool));
                    process.setExecutable(true);
                    definitions.addChildElement(process);
                    processes.add(new Pair<Process,Element>(process, pool));

                Participant participant = bpmnmodel.newInstance(Participant.class);
                    participant.setId("Participant_" + ArchiMateXMLLoader.removePrefix(pools.get(i)));
                    participant.setName(ArchiMateXMLLoader.getNameFromElement(pool));
                    participant.setProcess(process);
                    collaboration.addChildElement(participant);
                    participants.add(new Pair<Participant,Element>(participant, pool));
            }
        } else {
            // No Location / Pool therfore no collaboration, create sample process
            Process process =  bpmnmodel.newInstance(Process.class);
                process.setId(("Process_" + archiMateModelID).toUpperCase());
                process.setExecutable(true);
                definitions.addChildElement(process);
                processes.add(new Pair<Process,Element>(process, null));
        }

            // Draw pools
            for (Pair<Participant, Element> participantPair : participants) {
                Participant bpmParticipant = participantPair.getValue0();
                Element archiMateLocation = participantPair.getValue1();

                Map<String, Double> visuals = ArchiMateXMLLoader.getNodeDimensions(archiMateModel, archiMateLocation);
                BPMNElementCreator.drawPoolAndLane(bpmnmodel, bpmParticipant, visuals.get("x"), visuals.get("y"), visuals.get("w"), visuals.get("h"), true);
            }


        // Check if Businessroles / Lanes are given
        List<String> lanes = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "BusinessRole");
        List<Pair<Lane, Element>> lanePairs = new ArrayList<>();
        if (lanes.size()>0) { 
            // Find the corresponding process
            Process matchingProcess = null;
            for (int i = 0; i < lanes.size(); i++) {
                Element lane = ArchiMateXMLLoader.findElementById(archiMateModel, lanes.get(i));
                Element correspondingPool = ArchiMateXMLLoader.getLocationOfBusinessRole(archiMateModel, lane);

                if (correspondingPool != null) {
                    for (Pair<Process, Element> pair : processes) {
                        Element currentpool = pair.getValue1();
                        if (currentpool != null && currentpool.isEqualNode(correspondingPool)) {
                            matchingProcess = pair.getValue0();
                        }
                    }

                    // If no LaneSets exists for the process create one
                    if(matchingProcess.getLaneSets().isEmpty()) {
                        LaneSet laneSet = bpmnmodel.newInstance(LaneSet.class);
                            laneSet.setId("LaneSet_" + ArchiMateXMLLoader.removePrefix(correspondingPool.getAttribute("identifier")));
                            matchingProcess.addChildElement(laneSet);
                    }

                    LaneSet tmpLaneSet = (matchingProcess.getLaneSets().iterator().next());
                    Lane bpmnLane = bpmnmodel.newInstance(Lane.class);
                        bpmnLane.setId("Lane_" + ArchiMateXMLLoader.removePrefix(lanes.get(i)));
                        bpmnLane.setName(ArchiMateXMLLoader.getNameFromElement(lane));
                        tmpLaneSet.addChildElement(bpmnLane);
                        lanePairs.add(new Pair<Lane,Element>(bpmnLane, lane));

                    
                    // Draw the lane
                    Map<String, Double> poolVisuals = ArchiMateXMLLoader.getNodeDimensions(archiMateModel, correspondingPool);
                    Map<String, Double> laneVisuals = ArchiMateXMLLoader.getNodeDimensions(archiMateModel, lane);

                    if(tmpLaneSet.getLanes().size() == 1 && lanes.size() == 1) {
                        // only one lane -> use full size of pool
                        BPMNElementCreator.drawPoolAndLane(bpmnmodel, bpmnLane, poolVisuals.get("x")+30, poolVisuals.get("y"), poolVisuals.get("w")-30, poolVisuals.get("h"), true);
                    } else {
                        BPMNElementCreator.drawPoolAndLane(bpmnmodel, bpmnLane, poolVisuals.get("x")+30, laneVisuals.get("y"), poolVisuals.get("w")-30, laneVisuals.get("h"), true);
                    }


                    // Reset the found process
                    matchingProcess = null;
                }
            }
        }



        // Check if Business Functions are given and create subprocesses
        List<String> businessfunctions = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "BusinessFunction");
        List<Pair<SubProcess, Element>> subprocesses = new ArrayList<>();
        for(String businessfunction : businessfunctions) {
            BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessfunction), "SubProcess", SubProcess.class);
            SubProcess tmpSubProcess = bpmnmodel.getModelElementById(("SubProcess_" + ArchiMateXMLLoader.removePrefix(businessfunction)).toUpperCase());
            subprocesses.add(new Pair<SubProcess,Element>(tmpSubProcess, ArchiMateXMLLoader.findElementById(archiMateModel, businessfunction)));
            List<Pair<String,String>> allProperties = ArchiMateXMLLoader.getElementProperties(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, businessfunction));
            // The StandardLoopCharacteristics is not included in this prototype
            BPMNElementCreator.handleMultiInstanceLoopCharacteristics(bpmnmodel, tmpSubProcess, allProperties, businessfunction);
        }
        

        // Check if DataObjects are given and create the ioSpec of the corresponding process
        List<String> archiMateDataObjects = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "DataObject");
            for(String archiMateDataObject : archiMateDataObjects) {
                List<Pair<String,String>> allProperties = ArchiMateXMLLoader.getElementProperties(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, archiMateDataObject));
                Pair<String,String> subclassPair = allProperties.stream().filter(pair -> "subclass".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                if(subclassPair == null){
                    // Default DataObject
                    BPMNElementCreator.createDataObjectAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, processToIoSpecification, archiMateDataObject, "DataObject", DataObject.class);
                    DataObject tmpDataObject = bpmnmodel.getModelElementById("DataObject_" + ArchiMateXMLLoader.removePrefix(archiMateDataObject));
                        tmpDataObject.setName(ArchiMateXMLLoader.getNameFromElement(ArchiMateXMLLoader.findElementById(archiMateModel, archiMateDataObject)));
                        tmpDataObject.setCollection(BPMNElementCreator.isCollectionTrue(allProperties));
                        // The itemSubjectRef is not included in this prototype
                    
                    // Create correlating DataObjectReference
                    BPMNElementCreator.createDataObjectAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, processToIoSpecification, archiMateDataObject, "DataObjectReference", DataObjectReference.class);
                    DataObjectReference tmpDataObjectReference = bpmnmodel.getModelElementById("DataObjectReference_" + ArchiMateXMLLoader.removePrefix(archiMateDataObject));
                        tmpDataObjectReference.setName(ArchiMateXMLLoader.getNameFromElement(ArchiMateXMLLoader.findElementById(archiMateModel, archiMateDataObject)));
                        tmpDataObjectReference.setDataObject(tmpDataObject);
                        
                    // DataObject has Variables - create form
                    List<Pair<String,String>> allVariables = BPMNElementCreator.findFormFields(allProperties);
                    if(!allVariables.isEmpty()) {
                        ExtensionElements dataObRefExtensionElement = tmpDataObjectReference.getExtensionElements();
                        dataObRefExtensionElement.addChildElement(BPMNElementCreator.createCamundaForms(bpmnmodel, allVariables));
                    }
                } else {
                    switch(subclassPair.getValue1().toLowerCase()) {                             
                        case "datainput":
                            BPMNElementCreator.createDataObjectAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, processToIoSpecification, archiMateDataObject, "DataInput", DataInput.class);
                            DataInput tmpDataInput = bpmnmodel.getModelElementById("DataInput_" + ArchiMateXMLLoader.removePrefix(archiMateDataObject));
                                tmpDataInput.setName(ArchiMateXMLLoader.getNameFromElement(ArchiMateXMLLoader.findElementById(archiMateModel, archiMateDataObject)));
                                tmpDataInput.setCollection(BPMNElementCreator.isCollectionTrue(allProperties));
                                IoSpecification parentIoSpecification = (IoSpecification) tmpDataInput.getParentElement();
                                if(parentIoSpecification.getInputSets().size() == 0){
                                    // IoSpecification has now InputSet so create one
                                    InputSet tmpInputSet = bpmnmodel.newInstance(InputSet.class);
                                        tmpInputSet.setId("InputSet_" + parentIoSpecification.getId());
                                        parentIoSpecification.addChildElement(tmpInputSet);

                                        DataInputRefs tmpDataInputRef = bpmnmodel.newInstance(DataInputRefs.class);
                                            tmpInputSet.addChildElement(tmpDataInputRef);
                                            tmpDataInputRef.setTextContent(tmpDataInput.getId());
                                } else {
                                    // Get the InputSet of the IoSpecification
                                    InputSet tmpInputSet = parentIoSpecification.getInputSets().iterator().next();
                                    DataInputRefs tmpDataInputRef = bpmnmodel.newInstance(DataInputRefs.class);
                                            tmpInputSet.addChildElement(tmpDataInputRef);
                                            tmpDataInputRef.setTextContent(tmpDataInput.getId());
                                }

                                // IoSpecification must also have an OutputSet
                                if(parentIoSpecification.getOutputSets().size() == 0){
                                    OutputSet tmpOutputSet = bpmnmodel.newInstance(OutputSet.class);
                                        tmpOutputSet.setId("OutputSet_" + parentIoSpecification.getId());
                                        parentIoSpecification.addChildElement(tmpOutputSet);
                                }
                            // The itemSubjectRef is not included in this prototype
                            break;
                        case "dataoutput":
                            BPMNElementCreator.createDataObjectAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, processToIoSpecification, archiMateDataObject, "DataOutput", DataOutput.class);
                            DataOutput tmpDataOutput = bpmnmodel.getModelElementById("DataOutput_" + ArchiMateXMLLoader.removePrefix(archiMateDataObject));
                                tmpDataOutput.setName(ArchiMateXMLLoader.getNameFromElement(ArchiMateXMLLoader.findElementById(archiMateModel, archiMateDataObject)));
                                tmpDataOutput.setCollection(BPMNElementCreator.isCollectionTrue(allProperties));
                                IoSpecification oparentIoSpecification = (IoSpecification) tmpDataOutput.getParentElement();
                                if(oparentIoSpecification.getOutputSets().size() == 0){
                                    // IoSpecification has no OutputSet so create one
                                    OutputSet tmpOutputSet = bpmnmodel.newInstance(OutputSet.class);
                                    tmpOutputSet.setId("OutputSet_" + oparentIoSpecification.getId());
                                        oparentIoSpecification.addChildElement(tmpOutputSet);

                                        DataOutputRefs tmpDataOutputRef = bpmnmodel.newInstance(DataOutputRefs.class);
                                            tmpOutputSet.addChildElement(tmpDataOutputRef);
                                            tmpDataOutputRef.setTextContent(tmpDataOutput.getId());
                                } else {
                                    // Get the OutputSet of the IoSpecification
                                    OutputSet tmpOutputSet = oparentIoSpecification.getOutputSets().iterator().next();
                                    DataOutputRefs tmpDataOutputRef = bpmnmodel.newInstance(DataOutputRefs.class);
                                        tmpOutputSet.addChildElement(tmpDataOutputRef);
                                        tmpDataOutputRef.setTextContent(tmpDataOutput.getId());
                                }

                                // IoSpecification must also have an InputSet
                                if(oparentIoSpecification.getInputSets().size() == 0){
                                    InputSet tmpInputSet = bpmnmodel.newInstance(InputSet.class);
                                        tmpInputSet.setId("InputSet_" + oparentIoSpecification.getId());
                                        oparentIoSpecification.addChildElement(tmpInputSet);
                                }
                            // The itemSubjectRef is not included in this prototype
                            break;
                        default:
                            abort("Only DataInput or DataOutput is valid");
                    }
                }
            }


        // Check if Business Events are given and boundary Events
        List<String> businessEvents = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "BusinessEvent");
            for(String businessEvent : businessEvents) {
                List<Pair<String,String>> allProperties = ArchiMateXMLLoader.getElementProperties(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, businessEvent));
                Pair<String,String> subclassPair = allProperties.stream().filter(pair -> "subclass".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                Pair<String,String> boundaryPair = allProperties.stream().filter(pair -> "boundary".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);

                if(subclassPair == null){
                    abort("Syntax Error! The ArchiMate Concept [BusinessEvent] must contain an attribute called subclass with one of these values StartEvent; EndEvent; TerminateEndEvent; TimerEvent; MessageStartEvent; MessageEndEvent; CatchingMessageIntermediateEvent; ThrowingMessageIntermediateEvent");
                } else {
                    // all subclasses of the Event
                    if (boundaryPair == null) {
                        switch(subclassPair.getValue1().toLowerCase()) {
                            case "startevent":
                                // StartEvent has only an id and name
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "StartEvent", StartEvent.class);

                                // StartEvent has Variables - create form
                                List<Pair<String,String>> allVariables = BPMNElementCreator.findFormFields(allProperties);
                                if(!allVariables.isEmpty()) {
                                    StartEvent startEventob = bpmnmodel.getModelElementById("StartEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    ExtensionElements startEventExtensionElement = startEventob.getExtensionElements();
                                    startEventExtensionElement.addChildElement(BPMNElementCreator.createCamundaForms(bpmnmodel, allVariables));
                                }

                                break;
                            case "endevent":
                                // EndEvent has only an id and name
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "EndEvent", EndEvent.class);
                                break;
                            case "terminateendevent":
                                // The TerminateEndEvent is an EndEvent with a TerminateEventDefintion
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "TerminateEndEvent", EndEvent.class);
                                    EndEvent tmpEndEvent = bpmnmodel.getModelElementById("TerminateEndEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        TerminateEventDefinition tmpTerminateEventDefinition = bpmnmodel.newInstance(TerminateEventDefinition.class);
                                            tmpTerminateEventDefinition.setId("TerminateEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        tmpEndEvent.addChildElement(tmpTerminateEventDefinition);
                                break;
                            case "timerevent":
                                // The catching timer intermediate Event requires a TimerEventDefinition
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "TimerEvent", IntermediateCatchEvent.class);
                                IntermediateCatchEvent tmpTimerEvent = bpmnmodel.getModelElementById("TimerEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    TimerEventDefinition tmpTimerEventDefinition = bpmnmodel.newInstance(TimerEventDefinition.class);
                                        tmpTimerEventDefinition.setId("TimerEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        tmpTimerEvent.addChildElement(tmpTimerEventDefinition);
                                        for(Pair<String,String> singleProperty : allProperties){
                                            switch(singleProperty.getValue0().toLowerCase()){
                                                case "timedate":
                                                    TimeDate tmpTimeDate = bpmnmodel.newInstance(TimeDate.class);
                                                    tmpTimerEventDefinition.addChildElement(tmpTimeDate);
                                                    tmpTimeDate.setTextContent(singleProperty.getValue1());
                                                    break;
                                                case "timeduration":
                                                    TimeDuration tmpTimeDuration = bpmnmodel.newInstance(TimeDuration.class);
                                                    tmpTimerEventDefinition.addChildElement(tmpTimeDuration);
                                                    tmpTimeDuration.setTextContent(singleProperty.getValue1());
                                                    break;
                                                case "timecycle":
                                                    TimeCycle tmpTimeCycle = bpmnmodel.newInstance(TimeCycle.class);
                                                    tmpTimerEventDefinition.addChildElement(tmpTimeCycle);
                                                    tmpTimeCycle.setTextContent(singleProperty.getValue1());
                                                    break;
                                            }
                                        }
                                    break;
                            case "messagestartevent":
                                // The DataOutput and DataOutputAssociation is not included in this prototype
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "MessageStartEvent", StartEvent.class);
                                StartEvent tmpStartEvent = bpmnmodel.getModelElementById("MessageStartEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    MessageEventDefinition tmpMessageEventDefinition = bpmnmodel.newInstance(MessageEventDefinition.class);
                                        tmpMessageEventDefinition.setId("MessageEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        tmpStartEvent.addChildElement(tmpMessageEventDefinition);
                                        tmpMessageEventDefinition.setMessage(BPMNElementCreator.createAndAssignMessage(bpmnmodel, businessEvent));

                                    // MessageStartEvent has Variables - create form
                                    List<Pair<String,String>> allMsVariables = BPMNElementCreator.findFormFields(allProperties);
                                    if(!allMsVariables.isEmpty()) {
                                        ExtensionElements msstartEventExtensionElement = tmpStartEvent.getExtensionElements();
                                        msstartEventExtensionElement.addChildElement(BPMNElementCreator.createCamundaForms(bpmnmodel, allMsVariables));
                                    }
                                break;
                            case "messageendevent":
                                // The DataInput and DataInputAssociation is not included in this prototype
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "MessageEndEvent", EndEvent.class);
                                EndEvent tmpMessagEndEvent = bpmnmodel.getModelElementById("MessageEndEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    MessageEventDefinition tmpEndMessageEventDefinition = bpmnmodel.newInstance(MessageEventDefinition.class);
                                        tmpEndMessageEventDefinition.setId("MessageEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        tmpMessagEndEvent.addChildElement(tmpEndMessageEventDefinition);
                                        tmpEndMessageEventDefinition.setMessage(BPMNElementCreator.createAndAssignMessage(bpmnmodel, businessEvent));
                                break;
                            case "catchingmessageintermediateevent":
                                // The DataOutput and DataOutputAssociation is not included in this prototype
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "MessageIntermediateEvent", IntermediateCatchEvent.class);
                                IntermediateCatchEvent tmpIntermediateCatchEvent = bpmnmodel.getModelElementById("MessageIntermediateEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    MessageEventDefinition tmpCatchMessageEventDef = bpmnmodel.newInstance(MessageEventDefinition.class);
                                        tmpCatchMessageEventDef.setId("MessageEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        tmpIntermediateCatchEvent.addChildElement(tmpCatchMessageEventDef);
                                        tmpCatchMessageEventDef.setMessage(BPMNElementCreator.createAndAssignMessage(bpmnmodel, businessEvent));
                                break;
                            case "throwingmessageintermediateevent":
                                // The DataInput and DataInputAssociation is not included in this prototype
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "MessageIntermediateEvent", IntermediateThrowEvent.class);
                                IntermediateThrowEvent tmpIntermediateThrowEvent = bpmnmodel.getModelElementById("MessageIntermediateEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    MessageEventDefinition tmpThrowMessageEventDef = bpmnmodel.newInstance(MessageEventDefinition.class);
                                        tmpThrowMessageEventDef.setId("MessageEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        tmpIntermediateThrowEvent.addChildElement(tmpThrowMessageEventDef);
                                        tmpThrowMessageEventDef.setMessage(BPMNElementCreator.createAndAssignMessage(bpmnmodel, businessEvent));
                                break;

                            
                            default:
                                abort("Syntax Error! The ArchiMate Concept " + businessEvent + " must contain an attribute called subclass with one of these values StartEvent; EndEvent; TerminateEndEvent; TimerEvent; MessageStartEvent; MessageEndEvent; CatchingMessageIntermediateEvent; ThrowingMessageIntermediateEvent");
                        }
                    } else {
                        // Is bondary event - setAttachedTo will be set in the relationships
                        switch(subclassPair.getValue1().toLowerCase()) {
                            case "error": 
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "ErrorBoundaryEvent", BoundaryEvent.class);
                                BoundaryEvent tmpErrorEvent = bpmnmodel.getModelElementById("ErrorBoundaryEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                ErrorEventDefinition tmpErrorDF = bpmnmodel.newInstance(ErrorEventDefinition.class);
                                    tmpErrorDF.setId("ErrorEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    tmpErrorEvent.addChildElement(tmpErrorDF);
                                break;
                            case "timer":
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "TimerBoundaryEvent", BoundaryEvent.class);
                                BoundaryEvent tmpTimerEvent = bpmnmodel.getModelElementById("TimerBoundaryEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                tmpTimerEvent.setCancelActivity(BPMNElementCreator.interrupting(allProperties));
                                TimerEventDefinition tmpTimerEventDefinition = bpmnmodel.newInstance(TimerEventDefinition.class);
                                tmpTimerEventDefinition.setId("TimerEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                tmpTimerEvent.addChildElement(tmpTimerEventDefinition);
                                for(Pair<String,String> singleProperty : allProperties){
                                    switch(singleProperty.getValue0().toLowerCase()){
                                        case "timedate":
                                            TimeDate tmpTimeDate = bpmnmodel.newInstance(TimeDate.class);
                                            tmpTimerEventDefinition.addChildElement(tmpTimeDate);
                                            tmpTimeDate.setTextContent(singleProperty.getValue1());
                                            break;
                                        case "timeduration":
                                            TimeDuration tmpTimeDuration = bpmnmodel.newInstance(TimeDuration.class);
                                            tmpTimerEventDefinition.addChildElement(tmpTimeDuration);
                                            tmpTimeDuration.setTextContent(singleProperty.getValue1());
                                            break;
                                        case "timecycle":
                                            TimeCycle tmpTimeCycle = bpmnmodel.newInstance(TimeCycle.class);
                                            tmpTimerEventDefinition.addChildElement(tmpTimeCycle);
                                            tmpTimeCycle.setTextContent(singleProperty.getValue1());
                                            break;
                                    }
                                }
                                break;
                            case "message":
                                BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessEvent), "MessageBoundaryEvent", BoundaryEvent.class);
                                BoundaryEvent tmpMessageEvent = bpmnmodel.getModelElementById("MessageBoundaryEvent_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                    tmpMessageEvent.setCancelActivity(BPMNElementCreator.interrupting(allProperties));
                                    MessageEventDefinition tmpMessageDF = bpmnmodel.newInstance(MessageEventDefinition.class);
                                        tmpMessageDF.setId("MessageEventDefinition_" + ArchiMateXMLLoader.removePrefix(businessEvent));
                                        tmpMessageEvent.addChildElement(tmpMessageDF);
                                        tmpMessageDF.setMessage(BPMNElementCreator.createAndAssignMessage(bpmnmodel, businessEvent));
                                break;
                            default:
                                abort("Syntax Error! The ArchiMate Concept [BusinessProcess] " + businessEvent + " of Type 'boundary' must contain an attribute called 'subClass' with one of these values 'Error', 'Timer' or 'Message'.");
                        }
                    }
                }
            }

        
        // Check if Tasks are given
        List<String> businessProcesses = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "BusinessProcess");
        for(String businessProcess : businessProcesses) {
            List<Pair<String,String>> allProperties = ArchiMateXMLLoader.getElementProperties(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, businessProcess));
            Pair<String,String> subclassPair = allProperties.stream().filter(pair -> "subclass".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);

            if(subclassPair == null){
                abort("Syntax Error! The ArchiMate Concept [BusinessProcess] must contain an attribute called subclass with one of these values UserTask or CallAcitivity");
            } else {
                switch(subclassPair.getValue1().toLowerCase()) {
                    case "usertask":
                        BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessProcess), "UserTask", UserTask.class);
                        UserTask tmpUserTask = bpmnmodel.getModelElementById("UserTask_" + ArchiMateXMLLoader.removePrefix(businessProcess));
                        BPMNElementCreator.createIoSpecification(bpmnmodel, ArchiMateXMLLoader.removePrefix(businessProcess), tmpUserTask);
                        ExtensionElements userTaskExtensionElement = tmpUserTask.getExtensionElements();

                        CamundaProperties tCamundaProperties = bpmnmodel.newInstance(CamundaProperties.class);
                        userTaskExtensionElement.addChildElement(tCamundaProperties);

                        // OnCreate
                        Pair<String,String> onCreatePair = allProperties.stream().filter(pair -> "oncreate".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(onCreatePair != null) {
                            CamundaProperty onCreateProp = bpmnmodel.newInstance(CamundaProperty.class);
                                onCreateProp.setCamundaName("onCreate");
                                onCreateProp.setCamundaValue(onCreatePair.getValue1());
                                tCamundaProperties.addChildElement(onCreateProp);
                        }

                        // OnComplete
                        Pair<String,String> onCompletePair = allProperties.stream().filter(pair -> "oncomplete".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(onCompletePair != null) {
                            CamundaProperty onCompleteProp = bpmnmodel.newInstance(CamundaProperty.class);
                                onCompleteProp.setCamundaName("onComplete");
                                onCompleteProp.setCamundaValue(onCompletePair.getValue1());
                                tCamundaProperties.addChildElement(onCompleteProp);
                        }

                        // OnInit
                        Pair<String,String> onInitPair = allProperties.stream().filter(pair -> "oninit".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(onInitPair != null) {
                            CamundaProperty onInitProp = bpmnmodel.newInstance(CamundaProperty.class);
                                onInitProp.setCamundaName("onInit");
                                onInitProp.setCamundaValue(onInitPair.getValue1());
                                tCamundaProperties.addChildElement(onInitProp);
                        }

                        Pair<String,String> implementationPair = allProperties.stream().filter(pair -> "implementation".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(implementationPair != null) {
                            tmpUserTask.setImplementation(implementationPair.getValue1());
                        }
                        
                        // UserTask has Variables - create form
                        List<Pair<String,String>> allVariables = BPMNElementCreator.findFormFields(allProperties);
                        if(!allVariables.isEmpty()) {
                            userTaskExtensionElement.addChildElement(BPMNElementCreator.createCamundaForms(bpmnmodel, allVariables));
                        }

                        // The StandardLoopCharacteristics is not included in this prototype
                        BPMNElementCreator.handleMultiInstanceLoopCharacteristics(bpmnmodel, tmpUserTask, allProperties, businessProcess);
                        tmpUserTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "cockpit365.task_task.DefaultUserTask");
                       
                        break;
                    case "callactivity":
                        BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(businessProcess), "CallActivity", CallActivity.class);
                        CallActivity tmpCallActivity = bpmnmodel.getModelElementById("CallActivity_" + ArchiMateXMLLoader.removePrefix(businessProcess));
                        BPMNElementCreator.createIoSpecification(bpmnmodel, ArchiMateXMLLoader.removePrefix(businessProcess), tmpCallActivity);
                        // The StandardLoopCharacteristics is not included in this prototype
                        BPMNElementCreator.handleMultiInstanceLoopCharacteristics(bpmnmodel, tmpCallActivity, allProperties, businessProcess);

                        //The CallActivity will not be fully implemented in this prototype
                        break;
                    default:
                        abort("Syntax Error! The ArchiMate Concept [BusinessProcess] must contain an attribute called subclass with one of these values UserTask or CallAcitivity");
                }

            }

        }


        // Check if Service Tasks are given
        List<String> applicationFunctions = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "ApplicationFunction");
        for(String applicationFunction : applicationFunctions) {
            BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(applicationFunction), "ServiceTask", ServiceTask.class);
            ServiceTask tmpServiceTask = bpmnmodel.getModelElementById("ServiceTask_" + ArchiMateXMLLoader.removePrefix(applicationFunction));
            BPMNElementCreator.createIoSpecification(bpmnmodel, ArchiMateXMLLoader.removePrefix(applicationFunction), tmpServiceTask);
            // Camunda InOutput
            ExtensionElements extensionElements = tmpServiceTask.getExtensionElements();
            CamundaInputOutput tmpInputOutput = bpmnmodel.newInstance(CamundaInputOutput.class);
            extensionElements.addChildElement(tmpInputOutput);
            List<Pair<String,String>> allProperties = ArchiMateXMLLoader.getElementProperties(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, applicationFunction));
            Pair<String,String> implementationPair = allProperties.stream().filter(pair -> "implementation".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
            if(implementationPair != null) {
                switch(implementationPair.getValue1().toLowerCase()) {
                    case "standard":
                        tmpServiceTask.setImplementation(implementationPair.getValue1());
                        break;
                    case "javaclass":
                        Pair<String, String> camundaClass = allProperties.stream().filter(pair -> "class".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(camundaClass != null) {
                            tmpServiceTask.setCamundaClass(camundaClass.getValue1());
                        } else {
                            abort("If implementation is: 'JavaClass' then a property called 'class' must be present.");
                        }
                        break;
                    case "expression":
                        Pair<String, String> camundaExpression = allProperties.stream().filter(pair -> "expression".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        Pair<String, String> camundaResultVariable = allProperties.stream().filter(pair -> "resultvariable".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(camundaExpression == null || camundaResultVariable == null){
                            abort("If implementation is: 'Expression' then the properties 'Expression' and 'ResultVariable' must be present.");
                        } else {
                            tmpServiceTask.setCamundaExpression(camundaExpression.getValue1());
                            tmpServiceTask.setCamundaResultVariable(camundaResultVariable.getValue1());
                        }
                        break;
                    case "external":
                        Pair<String, String> camundaType= allProperties.stream().filter(pair -> "type".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        Pair<String, String> camundaTopic= allProperties.stream().filter(pair -> "topic".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(camundaType == null || camundaTopic == null){
                            tmpServiceTask.setCamundaType(camundaType.getValue1());
                            tmpServiceTask.setCamundaTopic(camundaTopic.getValue1());
                        } else {
                            abort("If implementation is: 'External' then the properties 'Type' and 'Topic' must be present.");
                        }
                        break;
                    case "delegateexpression":
                        Pair<String, String> camundaDelegateExpression = allProperties.stream().filter(pair -> "delegateexpression".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(camundaDelegateExpression == null) {
                            abort("If implementation is: 'DelegateExpression' then the property 'DelegateExpression' must be present.");
                        } else {
                            tmpServiceTask.setCamundaDelegateExpression(camundaDelegateExpression.getValue1());
                        }
                        break;
                    case "connector":
                        Pair<String, String> connectorID = allProperties.stream().filter(pair -> "connectorid".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
                        if(connectorID == null) {
                            abort("If implementation is: 'Connector' then the property 'ConnectorID' must be present.");
                        } else {
                            CamundaConnector camundaConnector = bpmnmodel.newInstance(CamundaConnector.class);
                                extensionElements.addChildElement(camundaConnector);
                                    CamundaConnectorId camundaConnectorId = bpmnmodel.newInstance(CamundaConnectorId.class);
                                        camundaConnector.addChildElement(camundaConnectorId);
                                            camundaConnectorId.setTextContent(connectorID.getValue1());
                        }
                        break;
                    default: 
                        abort("Syntax Error! The Service Task implementation must be one of the following values: 'Standard', 'JavaClass', 'Expression', 'External', 'DelegateExpression' or 'Conncetor'");
                }
            }

            Pair<String,String> templatePair = allProperties.stream().filter(pair -> "template".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);
            if(templatePair != null) {
                tmpServiceTask.setCamundaDelegateExpression("#{cockpit}");
                // All the possible templates
                switch(templatePair.getValue1().toLowerCase()) {
                    case "c365_appanalytics_updatetrackingentity":
                        ImplementedTemplates.c365_appanalytics_updatetrackingentity(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_appanalytics_linkpersontoanalyticsentity":
                        ImplementedTemplates.c365_appanalytics_linkpersontoanalyticsentity(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_snippetorganizationdefault_addselfserviceprocessrole":
                        ImplementedTemplates.c365_snippetorganizationdefault_addselfserviceprocessrole(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "aufgabe":
                        ImplementedTemplates.aufgabe(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "aufgabemitdetails":
                        ImplementedTemplates.aufgabemitdetails(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "aufgabemitstatus":
                        ImplementedTemplates.aufgabemitstatus(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccmsetup_basicrelationship01":
                        ImplementedTemplates.ccmsetup_basicrelationship01(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_snippetschedulebase_createevent":
                        ImplementedTemplates.c365_snippetschedulebase_createevent(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_createmessageforobject":
                        ImplementedTemplates.comm_createmessageforobject(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_snippetchatbase_createchatchannel":
                        ImplementedTemplates.c365_snippetchatbase_createchatchannel(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_snippetchatbase_postmsgtochannel":
                        ImplementedTemplates.c365_snippetchatbase_postmsgtochannel(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "testing_completeteststep":
                        ImplementedTemplates.testing_completeteststep(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_copydatadirectly":
                        ImplementedTemplates.ccm_copydatadirectly(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_createticket":
                        ImplementedTemplates.ccm_createticket(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "services_createchangeblock":
                        ImplementedTemplates.services_createchangeblock(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "services_createeventblock":
                        ImplementedTemplates.services_createeventblock(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "services_createincidentblock":
                        ImplementedTemplates.services_createincidentblock(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "services_createservicerequestblock":
                        ImplementedTemplates.services_createservicerequestblock(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_appasset_newhandover":
                        ImplementedTemplates.c365_appasset_newhandover(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_budgeting_budget_assignmenttask":
                        ImplementedTemplates.c365_budgeting_budget_assignmenttask(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "task_addentrytochecklist":
                        ImplementedTemplates.task_addentrytochecklist(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "content_createcontent":
                        ImplementedTemplates.content_createcontent(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "fico_costentry":
                        ImplementedTemplates.fico_costentry(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "organization_deputyform":
                        ImplementedTemplates.organization_deputyform(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "schedule_createevent":
                        ImplementedTemplates.schedule_createevent(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "testresult_create_ticket":
                        ImplementedTemplates.testresult_create_ticket(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "links_createlink":
                        ImplementedTemplates.links_createlink(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "services_cuinfranode":
                        ImplementedTemplates.services_cuinfranode(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_auditmanagement_auditpollfortag":
                        ImplementedTemplates.c365_auditmanagement_auditpollfortag(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_govhub_exposure":
                        ImplementedTemplates.c365_govhub_exposure(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_govhub_finding":
                        ImplementedTemplates.c365_govhub_finding(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "case_orderentry":
                        ImplementedTemplates.case_orderentry(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "organization_assignmentservicetask":
                        ImplementedTemplates.organization_assignmentservicetask(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_newuser":
                        ImplementedTemplates.ccm_newuser(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_createlink":
                        ImplementedTemplates.ccm_createlink(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "crmmanager":
                        ImplementedTemplates.crmmanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "case_orderfastentry":
                        ImplementedTemplates.case_orderfastentry(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "datacontainer_dumpprocex":
                        ImplementedTemplates.datacontainer_dumpprocex(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "hr_onboarding":
                        ImplementedTemplates.hr_onboarding(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itsm_event_management":
                        ImplementedTemplates.itsm_event_management(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "eventmgmt1":
                        ImplementedTemplates.eventmgmt1(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itsm_event_monitoring":
                        ImplementedTemplates.itsm_event_monitoring(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itsm_event_occurred":
                        ImplementedTemplates.itsm_event_occurred(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "testing_executetestcase":
                        ImplementedTemplates.testing_executetestcase(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_processesappx_getobjectdata":
                        ImplementedTemplates.c365_processesappx_getobjectdata(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_processesappx_getobjectdetails":
                        ImplementedTemplates.c365_processesappx_getobjectdetails(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "hrmanager":
                        ImplementedTemplates.hrmanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "hr_map":
                        ImplementedTemplates.hr_map(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "iamadmin":
                        ImplementedTemplates.iamadmin(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "iammanager":
                        ImplementedTemplates.iammanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_appdam_newaccount":
                        ImplementedTemplates.c365_appdam_newaccount(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "incident":
                        ImplementedTemplates.incident(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "inc01":
                        ImplementedTemplates.inc01(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itsm_incident_management":
                        ImplementedTemplates.itsm_incident_management(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "incidentmanager":
                        ImplementedTemplates.incidentmanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itsm_incident_registration":
                        ImplementedTemplates.itsm_incident_registration(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itsm_incident":
                        ImplementedTemplates.itsm_incident(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "invoicing":
                        ImplementedTemplates.invoicing(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itoperationsmanager":
                        ImplementedTemplates.itoperationsmanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "itoperator":
                        ImplementedTemplates.itoperator(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "aimanager":
                        ImplementedTemplates.aimanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "authoritycontact":
                        ImplementedTemplates.authoritycontact(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "prozess1":
                        ImplementedTemplates.prozess1(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "auditlog_logsensorvalue":
                        ImplementedTemplates.auditlog_logsensorvalue(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "magellan_simulation":
                        ImplementedTemplates.magellan_simulation(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "magellan_mat_simulation":
                        ImplementedTemplates.magellan_mat_simulation(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "magellan_processes_gapimport":
                        ImplementedTemplates.magellan_processes_gapimport(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "magellan_processes_kpiimport":
                        ImplementedTemplates.magellan_processes_kpiimport(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_sendemailwithparams":
                        ImplementedTemplates.comm_sendemailwithparams(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_sendemail_with_attachment":
                        ImplementedTemplates.comm_sendemail_with_attachment(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_sendmail_processrole":
                        ImplementedTemplates.comm_sendmail_processrole(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "govfinding":
                        ImplementedTemplates.govfinding(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "manage_org":
                        ImplementedTemplates.manage_org(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "message1":
                        ImplementedTemplates.message1(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "process_migratetolatestintent":
                        ImplementedTemplates.process_migratetolatestintent(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "employee":
                        ImplementedTemplates.employee(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "mitarbeitertrittein":
                        ImplementedTemplates.mitarbeitertrittein(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccmsetup_prepareapp":
                        ImplementedTemplates.ccmsetup_prepareapp(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ssm":
                        ImplementedTemplates.ssm(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "vorfallabwickeln":
                        ImplementedTemplates.vorfallabwickeln(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "processmanager":
                        ImplementedTemplates.processmanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_processesappx_processstartuniquegeneric":
                        ImplementedTemplates.c365_processesappx_processstartuniquegeneric(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_processesappx_processstartgeneric":
                        ImplementedTemplates.c365_processesappx_processstartgeneric(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "process_issueotatokenforuser":
                        ImplementedTemplates.process_issueotatokenforuser(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "process_triggereventintent":
                        ImplementedTemplates.process_triggereventintent(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "productmanager":
                        ImplementedTemplates.productmanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "portfoliomgr":
                        ImplementedTemplates.portfoliomgr(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "projectleader":
                        ImplementedTemplates.projectleader(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "publicwebsite":
                        ImplementedTemplates.publicwebsite(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_pullonlyasarray":
                        ImplementedTemplates.ccm_pullonlyasarray(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_appdam_getentitydetails":
                        ImplementedTemplates.c365_appdam_getentitydetails(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "magellan_processes_rmat":
                        ImplementedTemplates.magellan_processes_rmat(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "magellan_processes_rmat_masterdata":
                        ImplementedTemplates.magellan_processes_rmat_masterdata(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_createcallback":
                        ImplementedTemplates.comm_createcallback(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "testing_refreshtestresult":
                        ImplementedTemplates.testing_refreshtestresult(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_snippetorganizationdefault_refuserlogin":
                        ImplementedTemplates.c365_snippetorganizationdefault_refuserlogin(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "numeerator_getnextnumber":
                        ImplementedTemplates.numeerator_getnextnumber(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "organization_getapprover":
                        ImplementedTemplates.organization_getapprover(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_processesappx_unresolvedtasks":
                        ImplementedTemplates.c365_processesappx_unresolvedtasks(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "riskmanager":
                        ImplementedTemplates.riskmanager(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_runjob":
                        ImplementedTemplates.ccm_runjob(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_runquery":
                        ImplementedTemplates.ccm_runquery(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_runquerycount":
                        ImplementedTemplates.ccm_runquerycount(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "worker_runjob":
                        ImplementedTemplates.worker_runjob(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "testing_starttestrun":
                        ImplementedTemplates.testing_starttestrun(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_processesappx_searchkeywords":
                        ImplementedTemplates.c365_processesappx_searchkeywords(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_sendemail":
                        ImplementedTemplates.comm_sendemail(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "organization_sendtoassignments":
                        ImplementedTemplates.organization_sendtoassignments(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_sendmessage":
                        ImplementedTemplates.comm_sendmessage(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_sendmail_processrole_with_attachment":
                        ImplementedTemplates.comm_sendmail_processrole_with_attachment(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "comm_sendpushnotification":
                        ImplementedTemplates.comm_sendpushnotification(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_mailjetintegration_sendsms":
                        ImplementedTemplates.c365_mailjetintegration_sendsms(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_servicemanagement_reporting_requestsreport":
                        ImplementedTemplates.c365_servicemanagement_reporting_requestsreport(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "process_triggeractivitybyflowid":
                        ImplementedTemplates.process_triggeractivitybyflowid(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "remote_requestjobexecutionservice":
                        ImplementedTemplates.remote_requestjobexecutionservice(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "sysadmin_root":
                        ImplementedTemplates.sysadmin_root(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccmsetup_query01":
                        ImplementedTemplates.ccmsetup_query01(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "c365_processesappx_activitylog_trackevent":
                        ImplementedTemplates.c365_processesappx_activitylog_trackevent(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccmsetup_transferprofileentry01":
                        ImplementedTemplates.ccmsetup_transferprofileentry01(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_transitionstate":
                        ImplementedTemplates.ccm_transitionstate(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "audittrigger":
                        ImplementedTemplates.audittrigger(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "services_updateblock":
                        ImplementedTemplates.services_updateblock(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "services_updateeventblock":
                        ImplementedTemplates.services_updateeventblock(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "ccm_updateremotetooldata":
                        ImplementedTemplates.ccm_updateremotetooldata(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "status_updatestatuswithdatablock":
                        ImplementedTemplates.status_updatestatuswithdatablock(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "vorlagechangemodal":
                        ImplementedTemplates.vorlagechangemodal(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    case "vorlageincidentmodal":
                        ImplementedTemplates.vorlageincidentmodal(bpmnmodel, tmpServiceTask, extensionElements, tmpInputOutput, allProperties);
                        break;
                    default:
                        abort("The provided template is not known.");
                }
            }

            // The StandardLoopCharacteristics is not included in this prototype
            BPMNElementCreator.handleMultiInstanceLoopCharacteristics(bpmnmodel, tmpServiceTask, allProperties, applicationFunction);

        }
        

        // Check if ParallelGateways are given
        List<String> andJunctions = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "AndJunction");
        BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, andJunctions, "Gateway", ParallelGateway.class);


        // Check if Exclusive or Eventbased Gateways are given
        List<String> orJunctions = ArchiMateXMLLoader.returnAllElementsOfGivenType(archiMateModel, "OrJunction");
        for(String orJunction : orJunctions) {
            List<Pair<String,String>> allProperties = ArchiMateXMLLoader.getElementProperties(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, orJunction));
            Pair<String,String> subclassPair = allProperties.stream().filter(pair -> "subclass".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);

            if(subclassPair == null){
                abort("Syntax Error! The ArchiMate Concept [OrJunction] must contain an attribute called subclass with one of these values: ExclusiveGateway or EventBasedGateway");
            } else {
                // all subclasses of the Event
                switch(subclassPair.getValue1().toLowerCase()) {
                    case "exclusivegateway":
                        // ExclusiveGateway - attributes gatewayDirection and eventGatewayType and default are omitted
                        BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(orJunction), "ExclusiveGateway", ExclusiveGateway.class);
                        break;
                    case "eventbasedgateway":
                        // EndEvent - attributes gatewayDirection and eventGatewayType are omitted
                        BPMNElementCreator.archiMate2BPMNnameAndID(archiMateModel, bpmnmodel, processes, lanePairs, subprocesses, mappings, Collections.singletonList(orJunction), "EventbasedGateway", EventBasedGateway.class);
                        break;
                    default: 
                        abort("Syntax Error! The ArchiMate Concept [OrJunction] must contain an attribute called subclass with one of these values: ExclusiveGateway or EventBasedGateway");
                }
            }
        } 


        // Set the relationships
        List<Element> allTriggeringRelationships = ArchiMateXMLLoader.returnAllRelationshipsOfType(archiMateModel, "Triggering");
        for (Element triggeringRelationship : allTriggeringRelationships) {
            if(bpmnmodel.getModelElementById(ArchiMateXMLLoader.findBPMNIdByArchiMateId(mappings, triggeringRelationship.getAttribute("source"))) != null && bpmnmodel.getModelElementById(ArchiMateXMLLoader.findBPMNIdByArchiMateId(mappings, triggeringRelationship.getAttribute("target"))) != null) {
                List<Pair<String,String>> properiesForRelationship = ArchiMateXMLLoader.getElementProperties(archiMateModel, triggeringRelationship);
                Pair<String,String> boundaryPair = properiesForRelationship.stream().filter(pair -> "boundary".equals(pair.getValue0().toLowerCase())).findFirst().orElse(null);

                // Check if it is a boundary relation
                if(boundaryPair != null) {
                    Activity attachedToActivity = (Activity) bpmnmodel.getModelElementById(ArchiMateXMLLoader.findBPMNIdByArchiMateId(mappings, triggeringRelationship.getAttribute("source")));
                    BoundaryEvent boundaryEvent = (BoundaryEvent) bpmnmodel.getModelElementById(ArchiMateXMLLoader.findBPMNIdByArchiMateId(mappings, triggeringRelationship.getAttribute("target")));
                    boundaryEvent.setAttachedTo(attachedToActivity);
                    
                    // Move the already drawn Element to the correct spot
                    Bounds boundaryElementBounds = boundaryEvent.getDiagramElement().getBounds();
                    Shape activityShape = bpmnmodel.getModelElementsByType(BpmnShape.class).stream().filter(s -> s.getBpmnElement().equals(attachedToActivity)).findFirst().orElse(null);
                    if(activityShape !=null) {
                        Bounds activityBounds = activityShape.getBounds();
                        boundaryElementBounds.setY(activityBounds.getY()+activityBounds.getHeight()-18);
                    }

                } else {
                    SequenceFlow sequenceFlow = bpmnmodel.newInstance(SequenceFlow.class);
                    FlowNode from = (FlowNode) bpmnmodel.getModelElementById(ArchiMateXMLLoader.findBPMNIdByArchiMateId(mappings, triggeringRelationship.getAttribute("source")));
                    FlowNode to = (FlowNode) bpmnmodel.getModelElementById(ArchiMateXMLLoader.findBPMNIdByArchiMateId(mappings, triggeringRelationship.getAttribute("target")));
                        sequenceFlow.setId("Flow_" + ArchiMateXMLLoader.removePrefix(triggeringRelationship.getAttribute("identifier")));
                        sequenceFlow.setSource(from);
                        sequenceFlow.setTarget(to);
                        if(ArchiMateXMLLoader.getDocumentationFromElement(triggeringRelationship) != null) {
                            Documentation tmpDocumentationFlow = bpmnmodel.newInstance(Documentation.class);
                            tmpDocumentationFlow.setId("Documentation_" + ArchiMateXMLLoader.removePrefix(triggeringRelationship.getAttribute("identifier")));
                            tmpDocumentationFlow.setTextContent(ArchiMateXMLLoader.getDocumentationFromElement(triggeringRelationship));
                            sequenceFlow.getDocumentations().add(tmpDocumentationFlow);
                        }                   

                    Process tmpProcess = (Process) sequenceFlow.getSource().getParentElement();
                        tmpProcess.addChildElement(sequenceFlow);
                        from.getOutgoing().add(sequenceFlow);
                        to.getIncoming().add(sequenceFlow);

                    for(Pair<String,String> propertyForRelationship : properiesForRelationship) {
                        if(propertyForRelationship.getValue0().toLowerCase().equals("default") && propertyForRelationship.getValue1().toLowerCase().equals("true")){
                            if(sequenceFlow.getSource().getId().toLowerCase().startsWith("exclusivegateway")){
                                ExclusiveGateway tmpEXGW = (ExclusiveGateway) sequenceFlow.getSource();
                                tmpEXGW.setDefault(sequenceFlow);
                            }
                        }
                        if(propertyForRelationship.getValue0().toLowerCase().equals("conditionexpression")){
                            ConditionExpression tmpCondExpr = bpmnmodel.newInstance(ConditionExpression.class);
                            sequenceFlow.setConditionExpression(tmpCondExpr);
                            tmpCondExpr.setTextContent(propertyForRelationship.getValue1());
                        }
                        if(propertyForRelationship.getValue0().toLowerCase().equals("javascript")){ 
                            ExtensionElements sequenceFlowExtension = bpmnmodel.newInstance(ExtensionElements.class);
                            sequenceFlow.setExtensionElements(sequenceFlowExtension);
                            CamundaExecutionListener tmpCamundaExecutionListener = bpmnmodel.newInstance(CamundaExecutionListener.class);
                                tmpCamundaExecutionListener.setCamundaEvent("take");
                                sequenceFlowExtension.addChildElement(tmpCamundaExecutionListener);
                                CamundaScript script = bpmnmodel.newInstance(CamundaScript.class);
                                    script.setCamundaScriptFormat("javascript");
                                    tmpCamundaExecutionListener.addChildElement(script);
                                    script.setTextContent(propertyForRelationship.getValue1());
                        }

                    }

                    BpmnEdge edge = bpmnmodel.newInstance(BpmnEdge.class);
                        edge.setId(sequenceFlow.getId() + "_di");
                        edge.setBpmnElement(sequenceFlow);
                        edge.setSourceElement(from.getDiagramElement());
                        edge.setTargetElement(to.getDiagramElement());

                        Waypoint sourceWaypoint = bpmnmodel.newInstance(Waypoint.class);
                        Waypoint targetWaypoint = bpmnmodel.newInstance(Waypoint.class);

                        BpmnShape fromShape = bpmnmodel.getModelElementsByType(BpmnShape.class).stream().filter(s -> s.getBpmnElement().equals(from)).findFirst().orElse(null);
                        if(fromShape != null){
                            Bounds fromBounds = fromShape.getBounds();
                            //Map<String, Double> visualsSource = ArchiMateXMLLoader.getNodeDimensions(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, triggeringRelationship.getAttribute("source")));
                            switch(ArchiMateXMLLoader.findElementById(archiMateModel, triggeringRelationship.getAttribute("source")).getAttribute("xsi:type")) {
                                case "BusinessEvent":
                                    // 36x36 middle right
                                    sourceWaypoint.setX(fromBounds.getX()+36);
                                    sourceWaypoint.setY(fromBounds.getY()+18);
                                    break;
                                case "AndJunction":
                                    // 50x50 middle right
                                    sourceWaypoint.setX(fromBounds.getX()+50);
                                    sourceWaypoint.setY(fromBounds.getY()+25);
                                    break;
                                case "OrJunction":
                                    // 50x50 middle right
                                    sourceWaypoint.setX(fromBounds.getX()+50);
                                    sourceWaypoint.setY(fromBounds.getY()+25);
                                    break;
                                default:
                                    // middle right
                                    sourceWaypoint.setX(fromBounds.getX()+fromBounds.getWidth());
                                    sourceWaypoint.setY(fromBounds.getY()+(fromBounds.getHeight()/2));  
                            }
                        }

                        BpmnShape toShape = bpmnmodel.getModelElementsByType(BpmnShape.class).stream().filter(s -> s.getBpmnElement().equals(to)).findFirst().orElse(null);
                        //Map<String, Double> visualsTarget = ArchiMateXMLLoader.getNodeDimensions(archiMateModel, ArchiMateXMLLoader.findElementById(archiMateModel, triggeringRelationship.getAttribute("target")));
                        if(toShape != null){
                            Bounds toBounds = toShape.getBounds();
                            switch(ArchiMateXMLLoader.findElementById(archiMateModel, triggeringRelationship.getAttribute("target")).getAttribute("xsi:type")) {
                                    case "BusinessEvent":
                                        // 36x36 middle left
                                        targetWaypoint.setX(toBounds.getX());
                                        targetWaypoint.setY(toBounds.getY()+18);
                                        break;
                                    case "AndJunction":
                                        // 50x50 middle left
                                        targetWaypoint.setX(toBounds.getX());
                                        targetWaypoint.setY(toBounds.getY()+25);
                                        break;
                                    case "OrJunction":
                                        // 50x50 middle left
                                        targetWaypoint.setX(toBounds.getX());
                                        targetWaypoint.setY(toBounds.getY()+25);
                                        break;
                                    default:
                                        // middle left
                                        targetWaypoint.setX(toBounds.getX());
                                        targetWaypoint.setY(toBounds.getY()+(toBounds.getHeight()/2));
                                }
                            }

                            edge.addChildElement(sourceWaypoint);
                            edge.addChildElement(targetWaypoint);
                        plane.addChildElement(edge);
                }
            }
        }

        // The Access relations is not included in this prototype
        // A potential beginning of the implementation of Data-Associations is in DataAssociations.java


        // Validate the Model
        Bpmn.validateModel(bpmnmodel);


        // Save BPMN Model to file
        String outputFilePath = args[0];
        Bpmn.writeModelToFile(new File(outputFilePath.replace(".xml", ".bpmn")), bpmnmodel);

        System.out.println("BPMN model saved to " + outputFilePath.replace(".xml", ".bpmn"));
    }

    public static void printUsage() {
        System.out.println("Usage: java -jar archimate2bpmn-X.X.X-jar-with-dependencies.jar [ArchiMateModel.xml]");
    }

    public static void abort(String reason) {
        System.err.println("ERROR: " + reason);
        System.exit(1);
    }

}


