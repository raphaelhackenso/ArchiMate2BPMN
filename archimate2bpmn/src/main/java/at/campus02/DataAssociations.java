/* The Data-Association will not be included in this prototype, due to it's complexity as well as the limited support in the Camunda BPMN-Engine

// dataObject will always be the target
List<Element> allAccessRelationships = ArchiMateXMLLoader.returnAllRelationshipsOfType(archiMateModel, "Access");
for (Element accessRelationship : allAccessRelationships){
    if(bpmnmodel.getModelElementById(findBPMNIdByArchiMateId(mappings, accessRelationship.getAttribute("source"))) != null && bpmnmodel.getModelElementById(findBPMNIdByArchiMateId(mappings, accessRelationship.getAttribute("target"))) != null) {
        // Read = Input Write = Output
        if(accessRelationship.getAttribute("accessType").toLowerCase().equals("read")) {
            DataInputAssociation inputAssociation = bpmnmodel.newInstance(DataInputAssociation.class);
                inputAssociation.setId("Association_" + accessRelationship.getAttribute("identifier"));
                DataInput input = bpmnmodel.getModelElementById(findBPMNIdByArchiMateId(mappings, accessRelationship.getAttribute("target")));
                inputAssociation.getSources().add(input);

                FlowNode inputTarget = bpmnmodel.getModelElementById(findBPMNIdByArchiMateId(mappings, accessRelationship.getAttribute("source")));
                if(inputTarget instanceof ServiceTask) {
                    ServiceTask tmpServiceTask = (ServiceTask) inputTarget;
                        DataInput tmpServiceInput = bpmnmodel.newInstance(DataInput.class);
                            tmpServiceInput.setId("DataInput_" + accessRelationship.getAttribute("identifier"));
                            tmpServiceTask.getIoSpecification().addChildElement(tmpServiceInput);
                                DataInputRefs tmpServiceDIRefs = bpmnmodel.newInstance(DataInputRefs.class);
                                    tmpServiceTask.getIoSpecification().getInputSets().iterator().next().addChildElement(tmpServiceDIRefs);
                                        tmpServiceDIRefs.setTextContent(tmpServiceInput.getId());

                    inputAssociation.setTarget(tmpServiceInput);
                    tmpServiceTask.getDataInputAssociations().add(inputAssociation);

                }

                if (inputTarget instanceof EndEvent && inputTarget.getChildElementsByType(MessageEventDefinition.class).size() > 0) { 
                    // The Target of the DataInput is a MessageEndEvent
                    EndEvent tmpMessageEndEvent = (EndEvent) inputTarget;
                    DataInput tmpMessageEndEventInput = bpmnmodel.newInstance(DataInput.class);
                        tmpMessageEndEventInput.setId("DataInput_" + accessRelationship.getAttribute("identifier"));
                        tmpMessageEndEvent.addChildElement(tmpMessageEndEventInput);
                            DataInputRefs tmpMessageEndDIRefs = bpmnmodel.newInstance(DataInputRefs.class);
                            tmpMessageEndEvent.getInputSet().addChildElement(tmpMessageEndDIRefs);
                                tmpMessageEndDIRefs.setTextContent(tmpMessageEndEventInput.getId());
                    
                    inputAssociation.setTarget(tmpMessageEndEventInput);
                    tmpMessageEndEvent.getDataInputAssociations().add(inputAssociation);

                }

            

        } else if (accessRelationship.getAttribute("accessType").toLowerCase().equals("write")) {
            DataOutputAssociation outputAssociation = bpmnmodel.newInstance(DataOutputAssociation.class);
                outputAssociation.setId("Association_" + accessRelationship.getAttribute("identifier"));
                DataOutput output = bpmnmodel.getModelElementById(findBPMNIdByArchiMateId(mappings, accessRelationship.getAttribute("target")));
                outputAssociation.setTarget(output);

                FlowNode outputSource = bpmnmodel.getModelElementById(findBPMNIdByArchiMateId(mappings, accessRelationship.getAttribute("source")));
                if (outputSource instanceof StartEvent && outputSource.getChildElementsByType(MessageEventDefinition.class).size() > 0) {
                    // The Source of the DataOutput is a MessageStartEvent
                    StartEvent tmpMessageStartEvent = (StartEvent) outputSource;
                    DataOutput tmpMessageStartEventOutput = bpmnmodel.newInstance(DataOutput.class);
                        tmpMessageStartEventOutput.setId("DataOutput_" + accessRelationship.getAttribute("identifier"));
                        tmpMessageStartEvent.addChildElement(tmpMessageStartEventOutput);
                            DataOutputRefs tmpMessageStartDORefs = bpmnmodel.newInstance(DataOutputRefs.class);
                                tmpMessageStartEvent.getOutputSet().addChildElement(tmpMessageStartDORefs);
                                tmpMessageStartDORefs.setTextContent(tmpMessageStartEventOutput.getId());
                    
                    outputAssociation.getSources().add(tmpMessageStartEventOutput);
                    tmpMessageStartEvent.getDataOutputAssociations().add(outputAssociation);

                }

                if (outputSource instanceof IntermediateCatchEvent && outputSource.getChildElementsByType(MessageEventDefinition.class).size() > 0) {
                    // The Source of the DataOutput is a Catching MessageIntermediate Event
                    IntermediateCatchEvent tmpIntermediateCatchEvent = (IntermediateCatchEvent) outputSource;
                    DataOutput tmpIntermediateCatchEventOutput = bpmnmodel.newInstance(DataOutput.class);
                        tmpIntermediateCatchEventOutput.setId("DataOutput_" + accessRelationship.getAttribute("identifier"));
                        tmpIntermediateCatchEvent.addChildElement(tmpIntermediateCatchEventOutput);
                            DataOutputRefs tmpIntermediateCatchDORefs = bpmnmodel.newInstance(DataOutputRefs.class);
                                tmpIntermediateCatchEvent.getOutputSet().addChildElement(tmpIntermediateCatchDORefs);
                                tmpIntermediateCatchDORefs.setTextContent(tmpIntermediateCatchEventOutput.getId());

                    outputAssociation.getSources().add(tmpIntermediateCatchEventOutput);
                    tmpIntermediateCatchEvent.getDataOutputAssociations().add(outputAssociation);

                }
        }

    }
}
*/