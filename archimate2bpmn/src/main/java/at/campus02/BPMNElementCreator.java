package at.campus02;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.FlowNodeRef;
import org.camunda.bpm.model.bpmn.instance.Documentation;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InputSet;
import org.camunda.bpm.model.bpmn.instance.IoSpecification;
import org.camunda.bpm.model.bpmn.instance.ItemAwareElement;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.Message;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;
import org.camunda.bpm.model.bpmn.instance.OutputSet;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaFormData;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaFormField;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaValue;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Pair;

import java.util.Map;


import org.w3c.dom.*;

public class BPMNElementCreator {



    public static void archiMate2BPMNnameAndID(Document archiMateModel, BpmnModelInstance bpmnmodel,
            List<Pair<Process, Element>> processes,
            List<Pair<Lane, Element>> lanePairs,
            List<Pair<SubProcess, Element>> subprocesses,
            List<Pair<String, String>> mappings,
            List<String> elements, String type, Class<? extends FlowNode> bpmnElementType) {
        for (String elementId : elements) {
            Element archiElement = ArchiMateXMLLoader.findElementById(archiMateModel, elementId);
            FlowNode bpmnElement = bpmnmodel.newInstance(bpmnElementType);
            bpmnElement.setId(type + "_" + ArchiMateXMLLoader.removePrefix(elementId));
            bpmnElement.setName(ArchiMateXMLLoader.getNameFromElement(archiElement));
            if (ArchiMateXMLLoader.getDocumentationFromElement(archiElement) != null) {
                Documentation tmpDocumentation = bpmnmodel.newInstance(Documentation.class);
                tmpDocumentation.setTextContent(ArchiMateXMLLoader.getDocumentationFromElement(archiElement));
                tmpDocumentation.setId("Documentation_" + ArchiMateXMLLoader.removePrefix(elementId));
                bpmnElement.getDocumentations().add(tmpDocumentation);
            }

            List<Element> allIncomingElements = ArchiMateXMLLoader.returnAllIncomingElementsPoolLaneOnly(archiMateModel,
                    archiElement);
            if (allIncomingElements.isEmpty()) {
                // Not in pool or lane, therefore use default process
                Process tmpProcess = BPMNElementCreator.findProcessWithNullElement(processes).getValue0();
                tmpProcess.addChildElement(bpmnElement);
            } else {
                for (Element singleIncomingElement : allIncomingElements) {
                    if ("BusinessRole".equals(singleIncomingElement.getAttribute("xsi:type"))) {
                        // Is in a Lane - add in process of lane + flowNodeRef
                        Lane tmpLane = BPMNElementCreator.findContainingLane(lanePairs, singleIncomingElement);
                        tmpLane.getParentElement().getParentElement().addChildElement(bpmnElement);
                        FlowNodeRef tmpFlowNodeRef = bpmnmodel.newInstance(FlowNodeRef.class);
                        tmpFlowNodeRef.setTextContent(bpmnElement.getId());
                        tmpLane.addChildElement(tmpFlowNodeRef);
                    } else if ("Location".equals(singleIncomingElement.getAttribute("xsi:type"))) {
                        // Is in a pool
                        Process tmpProcess = BPMNElementCreator.findContainingProcess(processes, singleIncomingElement);
                        tmpProcess.addChildElement(bpmnElement);
                    } else if ("BusinessFunction".equals(singleIncomingElement.getAttribute("xsi:type"))) {
                        // Is in a Subprocess
                        SubProcess tmpSubProcess = BPMNElementCreator.findContainingSubProcess(subprocesses,
                                singleIncomingElement);
                        tmpSubProcess.addChildElement(bpmnElement);
                    }
                }
            }

            ExtensionElements extensionElements = bpmnmodel.newInstance(ExtensionElements.class);
            bpmnElement.setExtensionElements(extensionElements);

            // Add to the mappings List 0=ArchiMateID 1=BPMNId
            mappings.add(new Pair<String, String>(elementId, bpmnElement.getId()));

            // Draw element
            Map<String, Double> visuals = ArchiMateXMLLoader.getNodeDimensions(archiMateModel, archiElement);
            switch (archiElement.getAttribute("xsi:type")) {
                case "BusinessEvent":
                    // Events are 36 by 36
                    BPMNElementCreator.drawElement(bpmnmodel, bpmnElement, visuals.get("x"), visuals.get("y"), 36, 36);
                    break;
                case "AndJunction":
                    // Gateways are 50 by 50
                    BPMNElementCreator.drawElement(bpmnmodel, bpmnElement, visuals.get("x"), visuals.get("y"), 50, 50);
                    break;
                case "OrJunction":
                    // Gateways are 50 by 50
                    BPMNElementCreator.drawElement(bpmnmodel, bpmnElement, visuals.get("x"), visuals.get("y"), 50, 50);
                    break;
                default:
                    BPMNElementCreator.drawElement(bpmnmodel, bpmnElement, visuals.get("x"), visuals.get("y"),
                            visuals.get("w"), visuals.get("h"));
            }
        }
    }



    public static void createDataObjectAndID(Document archiMateModel, BpmnModelInstance bpmnmodel,
            List<Pair<Process, Element>> processes,
            List<Pair<Lane, Element>> lanePairs,
            List<Pair<SubProcess, Element>> subprocesses,
            List<Pair<String, String>> mappings,
            List<Pair<Process, IoSpecification>> processToIoSpecification,
            String archiMateDataObjectID, String type, Class<? extends ItemAwareElement> bpmnDataObjectType) {
        Element archiMateDataObject = ArchiMateXMLLoader.findElementById(archiMateModel, archiMateDataObjectID);
        ItemAwareElement bpmnDataObject = bpmnmodel.newInstance(bpmnDataObjectType);
        bpmnDataObject.setId(type + "_" + ArchiMateXMLLoader.removePrefix(archiMateDataObjectID));

        List<Element> allIncomingElements = ArchiMateXMLLoader.returnAllIncomingElementsPoolLaneOnly(archiMateModel,
                archiMateDataObject);
        if (allIncomingElements.isEmpty()) {
            // Not in pool or lane, therefore use default process
            Process tmpProcess = BPMNElementCreator.findProcessWithNullElement(processes).getValue0();
            if (type.equals("DataInput") || type.equals("DataOutput")) {
                if (tmpProcess.getIoSpecification() == null) {
                    // Process has no IoSpecification but DataInput and DataOutput need one
                    IoSpecification tmpIoSpecification = bpmnmodel.newInstance(IoSpecification.class);
                    tmpIoSpecification.setId("IoSpecification" + tmpProcess.getId().substring(7));
                    tmpProcess.setIoSpecification(tmpIoSpecification);
                    processToIoSpecification.add(new Pair<Process, IoSpecification>(tmpProcess, tmpIoSpecification));
                    tmpIoSpecification.addChildElement(bpmnDataObject);
                } else {
                    // Process has IoSpecification
                    tmpProcess.getIoSpecification().addChildElement(bpmnDataObject);
                }
            } else {
                // Add DataObject and DataObjectReference automatically
                tmpProcess.addChildElement(bpmnDataObject);
            }

        } else {
            for (Element singleIncomingElement : allIncomingElements) {
                if ("BusinessRole".equals(singleIncomingElement.getAttribute("xsi:type"))) {
                    // Is in a Lane - add in process
                    Lane tmpLane = BPMNElementCreator.findContainingLane(lanePairs, singleIncomingElement);
                    Process tmpProcess = (Process) tmpLane.getParentElement().getParentElement();
                    if (type.equals("DataInput") || type.equals("DataOutput")) {
                        if (tmpProcess.getIoSpecification() == null) {
                            // Process has no IoSpecification but DataInput and DataOutput need one
                            IoSpecification tmpIoSpecification = bpmnmodel.newInstance(IoSpecification.class);
                            tmpIoSpecification.setId("IoSpecification" + tmpProcess.getId().substring(7));
                            tmpProcess.setIoSpecification(tmpIoSpecification);
                            processToIoSpecification
                                    .add(new Pair<Process, IoSpecification>(tmpProcess, tmpIoSpecification));
                            tmpIoSpecification.addChildElement(bpmnDataObject);
                        } else {
                            // Process has IoSpecification
                            tmpProcess.getIoSpecification().addChildElement(bpmnDataObject);
                        }
                    } else {
                        // Add DataObject and DataObjectReference automatically
                        tmpProcess.addChildElement(bpmnDataObject);
                    }
                } else if ("Location".equals(singleIncomingElement.getAttribute("xsi:type"))) {
                    // Is in a pool
                    Process tmpProcess = BPMNElementCreator.findContainingProcess(processes, singleIncomingElement);
                    if (type.equals("DataInput") || type.equals("DataOutput")) {
                        if (tmpProcess.getIoSpecification() == null) {
                            // Process has no IoSpecification but DataInput and DataOutput need one
                            IoSpecification tmpIoSpecification = bpmnmodel.newInstance(IoSpecification.class);
                            tmpIoSpecification.setId("IoSpecification" + tmpProcess.getId().substring(7));
                            tmpProcess.setIoSpecification(tmpIoSpecification);
                            processToIoSpecification
                                    .add(new Pair<Process, IoSpecification>(tmpProcess, tmpIoSpecification));
                            tmpIoSpecification.addChildElement(bpmnDataObject);
                        } else {
                            // Process has IoSpecification
                            tmpProcess.getIoSpecification().addChildElement(bpmnDataObject);
                        }
                    } else {
                        // Add DataObject and DataObjectReference automatically
                        tmpProcess.addChildElement(bpmnDataObject);
                    }
                } else if ("BusinessFunction".equals(singleIncomingElement.getAttribute("xsi:type"))) {
                    // Is in a Subprocess
                    SubProcess tmpSubProcess = BPMNElementCreator.findContainingSubProcess(subprocesses,
                            singleIncomingElement);
                    tmpSubProcess.addChildElement(bpmnDataObject);
                }
            }
        }

        ExtensionElements extensionElements = bpmnmodel.newInstance(ExtensionElements.class);
        bpmnDataObject.setExtensionElements(extensionElements);

        // Add to the mappings List 0=ArchiMateID 1=BPMNId
        mappings.add(new Pair<String, String>(archiMateDataObjectID, bpmnDataObject.getId()));

        // Draw element - the DataObject cannot be visualised, but Input Output and
        // DataObjectReference
        if (!type.equals("DataObject")) {
            Map<String, Double> visuals = ArchiMateXMLLoader.getNodeDimensions(archiMateModel, archiMateDataObject);
            BPMNElementCreator.drawElement(bpmnmodel, bpmnDataObject, visuals.get("x"), visuals.get("y"),
                    visuals.get("w"), visuals.get("h"));
        }

    }


    public static Message createAndAssignMessage(BpmnModelInstance bpmnmodel, String businessEventId) {
        Message tmpMessage = bpmnmodel.newInstance(Message.class);
        tmpMessage.setId("Message_" + ArchiMateXMLLoader.removePrefix(businessEventId));
        tmpMessage.setName("Message_" + ArchiMateXMLLoader.removePrefix(businessEventId));
        bpmnmodel.getDefinitions().addChildElement(tmpMessage);
        return tmpMessage;
    }
    


    public static BpmnPlane addCollaborationToPlane(BpmnModelInstance bpmnModel, BaseElement bpmnElement) {
        BpmnPlane plane = bpmnModel.getModelElementsByType(BpmnPlane.class).iterator().next();
        plane.setBpmnElement(bpmnElement);
        return plane;
    }


    public static BpmnShape drawPoolAndLane(BpmnModelInstance bpmnModel, BaseElement bpmnElement, double x, double y, double width, double height, boolean isHorizontal) {
        BpmnPlane plane = bpmnModel.getModelElementsByType(BpmnPlane.class).iterator().next();
        BpmnShape shape = bpmnModel.newInstance(BpmnShape.class);
            shape.setBpmnElement(bpmnElement);
            shape.setHorizontal(isHorizontal);
        Bounds bounds = bpmnModel.newInstance(Bounds.class);
            bounds.setX(x);
            bounds.setY(y);
            bounds.setWidth(width);
            bounds.setHeight(height);
            shape.setBounds(bounds);
            plane.addChildElement(shape);
        return shape;
    }

    public static BpmnShape drawElement(BpmnModelInstance bpmnModel, BaseElement bpmnElement, double x, double y, double width, double height) {
        BpmnPlane plane = bpmnModel.getModelElementsByType(BpmnPlane.class).iterator().next();
        BpmnShape shape = bpmnModel.newInstance(BpmnShape.class);
            shape.setId(bpmnElement.getId() + "_di");
            shape.setBpmnElement(bpmnElement);
            shape.setExpanded(true);
        Bounds bounds = bpmnModel.newInstance(Bounds.class);
            bounds.setX(x);
            bounds.setY(y);
            bounds.setWidth(width);
            bounds.setHeight(height);
            shape.setBounds(bounds);
            plane.addChildElement(shape);
        return shape;
    }



    public static Pair<Process, Element> findProcessWithNullElement(List<Pair<Process, Element>> processes) {
        for (Pair<Process, Element> pair : processes) {
            if (pair.getValue0() != null && pair.getValue1() == null) {
                return pair;
            }
        }
        return null;
    }


    public static Lane findContainingLane(List<Pair<Lane, Element>> lanePairs, Element businessRole) {
        for (Pair<Lane, Element> pair : lanePairs) {
            if (pair.getValue0() != null && pair.getValue1() == businessRole) {
                return pair.getValue0();
            }
        }
        return null;
    }


    public static Process findContainingProcess(List<Pair<Process, Element>> processPairs, Element location) {
        for (Pair<Process, Element> pair : processPairs) {
            if (pair.getValue0() != null && pair.getValue1() == location) {
                return pair.getValue0();
            }
        }
        return null;
    }


    public static SubProcess findContainingSubProcess(List<Pair<SubProcess, Element>> subprocessPairs, Element businessFunction) {
        for (Pair<SubProcess, Element> pair : subprocessPairs) {
            if (pair.getValue0() != null && pair.getValue1() == businessFunction) {
                return pair.getValue0();
            }
        }
        return null;
    }


    public static void createIoSpecification(BpmnModelInstance bpmnmodel, String id, FlowNode bpmnElement) {
        IoSpecification ioSpecification = bpmnmodel.newInstance(IoSpecification.class);
        ioSpecification.setId("IoSpecification_" + id);
        bpmnElement.addChildElement(ioSpecification);

        InputSet inputSet = bpmnmodel.newInstance(InputSet.class);
        inputSet.setId("InputSet_" + id);
        ioSpecification.addChildElement(inputSet);

        OutputSet outputSet = bpmnmodel.newInstance(OutputSet.class);
        outputSet.setId("OutputSet_" + id);
        ioSpecification.addChildElement(outputSet);
    }


    // Method to handle MultiInstanceLoopCharacteristics
    public static void handleMultiInstanceLoopCharacteristics(BpmnModelInstance bpmnmodel, FlowNode flowNode, List<Pair<String, String>> allProperties, String applicationFunction) {
        // Find MultiInstanceLoopCharacteristics property
        Pair<String, String> multiInstanceLoopCharacteristicsPair = allProperties.stream()
            .filter(pair -> "multiinstanceloopcharacteristics".equals(pair.getValue0().toLowerCase()))
            .findFirst()
            .orElse(null);

        if (multiInstanceLoopCharacteristicsPair != null) {
            // Create MultiInstanceLoopCharacteristics instance
            MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = bpmnmodel.newInstance(MultiInstanceLoopCharacteristics.class);
            multiInstanceLoopCharacteristics.setId("multiInstanceLoopCharacteristics_" + ArchiMateXMLLoader.removePrefix(applicationFunction));
            multiInstanceLoopCharacteristics.setSequential(isSequentialTrue(allProperties));

            // Check and set the collection property
            Pair<String, String> collection = allProperties.stream()
                    .filter(pair -> "collection".equals(pair.getValue0().toLowerCase()))
                    .findFirst()
                    .orElse(null);
            if (collection != null) {
                multiInstanceLoopCharacteristics.setCamundaCollection(collection.getValue1());
            } else {
                Main.abort("If multiInstanceLoopCharacteristics is given then a property called 'collection' must be present.");
            }

            // Check and set the element variable property
            Pair<String, String> elementVariable = allProperties.stream()
                    .filter(pair -> "elementvariable".equals(pair.getValue0().toLowerCase()))
                    .findFirst()
                    .orElse(null);
            if (elementVariable != null) {
                multiInstanceLoopCharacteristics.setCamundaElementVariable(elementVariable.getValue1());
            } else {
                Main.abort("If multiInstanceLoopCharacteristics is given then a property called 'elementVariable' must be present.");
            }
            flowNode.addChildElement(multiInstanceLoopCharacteristics);
        }
    }

    public static CamundaFormData createCamundaForms(BpmnModelInstance bpmnmodel, List<Pair<String, String>> allVariables) {
        CamundaFormData tmpCamundaFormData = bpmnmodel.newInstance(CamundaFormData.class);

        for(Pair<String,String> variable : allVariables) {
            CamundaFormField tmpCamundaFormField = bpmnmodel.newInstance(CamundaFormField.class);
            tmpCamundaFormData.getCamundaFormFields().add(tmpCamundaFormField);

            CamundaProperties tmpCamundaProperties = bpmnmodel.newInstance(CamundaProperties.class);
            tmpCamundaFormField.setCamundaProperties(tmpCamundaProperties);

            String typeAndName = variable.getValue0();
            String typeOnly = typeAndName.substring(0, typeAndName.indexOf(":")).toLowerCase();
            String nameOnly = typeAndName.substring(typeAndName.indexOf(":") + 1);
            String defaultValueOrContent = variable.getValue1();

            switch (typeOnly) {
                case "staticcontent":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    CamundaProperty controlStaticcontent = bpmnmodel.newInstance(CamundaProperty.class);
                        controlStaticcontent.setCamundaId("control");
                        controlStaticcontent.setCamundaValue("staticcontent");
                        tmpCamundaProperties.getCamundaProperties().add(controlStaticcontent);
                    break;
                case "string":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType("string");
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    CamundaProperty controlString = bpmnmodel.newInstance(CamundaProperty.class);
                        controlString.setCamundaId("control");
                        controlString.setCamundaValue(typeOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlString);
                    break;
                case "subform":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    CamundaProperty controlSubform = bpmnmodel.newInstance(CamundaProperty.class);
                        controlSubform.setCamundaId("control");
                        controlSubform.setCamundaValue(typeOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlSubform);
                    CamundaProperty subFormRefID = bpmnmodel.newInstance(CamundaProperty.class);
                        subFormRefID.setCamundaId("subformRefId");
                        subFormRefID.setCamundaValue(defaultValueOrContent);
                        tmpCamundaProperties.getCamundaProperties().add(subFormRefID);
                    break;
                case "boolean":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    CamundaProperty controlBoolean = bpmnmodel.newInstance(CamundaProperty.class);
                        controlBoolean.setCamundaId("control");
                        controlBoolean.setCamundaValue(typeOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlBoolean);
                    break;
                case "expression":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    CamundaProperty formUpdateExpression = bpmnmodel.newInstance(CamundaProperty.class);
                        formUpdateExpression.setCamundaId("formUpdateExpression");
                        formUpdateExpression.setCamundaValue(defaultValueOrContent);
                        tmpCamundaProperties.getCamundaProperties().add(formUpdateExpression);
                    break;
                case "date":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    CamundaProperty controlDate = bpmnmodel.newInstance(CamundaProperty.class);
                        controlDate.setCamundaId("control");
                        controlDate.setCamundaValue(typeOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlDate);
                    break;
                case "enum":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType("enum");
                    CamundaProperty controlEnum = bpmnmodel.newInstance(CamundaProperty.class);
                        controlEnum.setCamundaId("control");
                        controlEnum.setCamundaValue(typeOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlEnum);
                    CamundaProperty controlItemIdentifier = bpmnmodel.newInstance(CamundaProperty.class);
                        controlItemIdentifier.setCamundaId("itemIdentifier");
                        controlItemIdentifier.setCamundaValue(nameOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlItemIdentifier);
                    if(defaultValueOrContent == null || defaultValueOrContent.trim().isEmpty()){
                        Main.abort("No values as X:LabelforX;Y:LabelforY provided");
                    } else {
                        String[] pairs = defaultValueOrContent.split(";");
                        
                        for(String pair : pairs){
                            if (pair == null || pair.trim().isEmpty()) continue;
                            String[] parts = pair.split(":");
                            if (parts.length != 2) continue;
                            
                            String id = parts[0].trim();
                            String name = parts[1].trim();

                            CamundaValue camValue = bpmnmodel.newInstance(CamundaValue.class);
                                camValue.setCamundaId(id);
                                camValue.setCamundaName(name);
                            tmpCamundaFormField.getCamundaValues().add(camValue);
                        }
                    }
                    break;
                case "object":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    CamundaProperty entitiesFilterObj = bpmnmodel.newInstance(CamundaProperty.class);
                        entitiesFilterObj.setCamundaId("entitiesFilter");
                        entitiesFilterObj.setCamundaValue(defaultValueOrContent);
                        tmpCamundaProperties.getCamundaProperties().add(entitiesFilterObj);
                    if(defaultValueOrContent == null || defaultValueOrContent.trim().isEmpty()){
                        Main.abort("No values as X:Label X;Y:Label Y provided");
                    } else {
                        String[] pairs = defaultValueOrContent.split(";");
                        
                        for(String pair : pairs){
                            if (pair == null || pair.trim().isEmpty()) continue;
                            String[] parts = pair.split(":");
                            if (parts.length != 2) continue;
                            
                            String id = parts[0].trim();
                            String name = parts[1].trim();

                            CamundaValue camValue = bpmnmodel.newInstance(CamundaValue.class);
                                camValue.setCamundaId(id);
                                camValue.setCamundaName(name);
                            tmpCamundaFormField.getCamundaValues().add(camValue);
                        }
                    }
                    break;
                case "file":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    CamundaProperty multiple = bpmnmodel.newInstance(CamundaProperty.class);
                        multiple.setCamundaId("multiple");
                        multiple.setCamundaValue("true");
                        tmpCamundaProperties.getCamundaProperties().add(multiple);
                    break;
                case "files":
                        tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType(typeOnly);
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    CamundaProperty multipleFiles = bpmnmodel.newInstance(CamundaProperty.class);
                        multipleFiles.setCamundaId("multiple");
                        multipleFiles.setCamundaValue("true");
                        tmpCamundaProperties.getCamundaProperties().add(multipleFiles);
                    break;
                    case "richtext":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType("string");
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    CamundaProperty controlRichttext = bpmnmodel.newInstance(CamundaProperty.class);
                        controlRichttext.setCamundaId("control");
                        controlRichttext.setCamundaValue(typeOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlRichttext);
                    break;
                case "orgchart":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType("object");
                    CamundaProperty controlOrgChart = bpmnmodel.newInstance(CamundaProperty.class);
                        controlOrgChart.setCamundaId("control");
                        controlOrgChart.setCamundaId(typeOnly);
                        tmpCamundaProperties.getCamundaProperties().add(controlOrgChart);
                    CamundaProperty entitiesFilterOrChart = bpmnmodel.newInstance(CamundaProperty.class);
                        entitiesFilterOrChart.setCamundaId("entitiesFilter");
                        entitiesFilterOrChart.setCamundaValue(defaultValueOrContent);
                        tmpCamundaProperties.getCamundaProperties().add(entitiesFilterOrChart);
                    break;
                case "booleanswitch":
                    tmpCamundaFormField.setCamundaId(nameOnly);
                    tmpCamundaFormField.setCamundaLabel(nameOnly);
                    tmpCamundaFormField.setCamundaType("boolean");
                    tmpCamundaFormField.setCamundaDefaultValue(defaultValueOrContent);
                    CamundaProperty triggerUpdateFormBoolean = bpmnmodel.newInstance(CamundaProperty.class);
                        triggerUpdateFormBoolean.setCamundaId("triggerFormUpdate");
                        triggerUpdateFormBoolean.setCamundaValue("true");
                        tmpCamundaProperties.getCamundaProperties().add(triggerUpdateFormBoolean);
                    break;
                default:
                    Main.abort("Unknown Type");
            }



        }



        return tmpCamundaFormData;
    }


    public static List<Pair<String, String>> findFormFields(List<Pair<String, String>> pairs) {
        String[] possibleTypes = {"staticcontent", "string", "subform", "boolean", "expression", "date", "enum", "object", "file", "files", "richtext", "orgchart" ,"booleanswitch"};
        List<Pair<String, String>> returnList = new ArrayList<>();

        for (Pair<String, String> pair : pairs) {
            String typeAndName = pair.getValue0().toLowerCase();        
            int indexOf = typeAndName.indexOf(":");

            if(indexOf != -1) {
                if (Arrays.asList(possibleTypes).contains(typeAndName.substring(0, indexOf))) {
                    returnList.add(pair);
                }
            }
        }
        return returnList;
    }



    public static boolean isSequentialTrue(List<Pair<String, String>> pairs) {
        for (Pair<String, String> pair : pairs) {
            if ("issequential".equals(pair.getValue0().toLowerCase()) && "true".equals(pair.getValue1().toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    public static boolean isCollectionTrue(List<Pair<String, String>> pairs) {
        for (Pair<String, String> pair : pairs) {
            if ("iscollection".equals(pair.getValue0().toLowerCase()) && "true".equals(pair.getValue1().toLowerCase())) {
                return true;
            }
        }
        return false;
    }



    public static boolean interrupting(List<Pair<String, String>> pairs) {
        for (Pair<String, String> pair : pairs) {
            if ("interrupting".equals(pair.getValue0().toLowerCase()) && "false".equals(pair.getValue1().toLowerCase())) {
                return false;
            }
        }
        // Per Standard the default is 'true' - so a boundary event is per default interrupting
        return true;
    }






}