package at.campus02;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.util.HashMap;
import java.util.Map;
import org.javatuples.Pair;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class ArchiMateXMLLoader {
    public static Document getXMLDocument(String pathToXML) throws Exception {
        try {
            // Load the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pathToXML);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Element findElementById(Document archiMateModel, String id) {
        NodeList elements = archiMateModel.getElementsByTagName("element");
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            String identifier = element.getAttribute("identifier");
            if (identifier.equals(id)) {
                return element;
            }
        }
        return null;
    }


    public static String getNameFromElement(Element element) {
        if (element == null) {
            return null;
        }
        // Get the first <name> child element
        NodeList nameNodes = element.getElementsByTagName("name");
        if (nameNodes.getLength() > 0) {
            Element nameElement = (Element) nameNodes.item(0);
            return nameElement.getTextContent().trim();
        }
        return null;
    }

    public static String getDocumentationFromElement(Element element) {
        if (element == null) {
            return null;
        }
        // Get the first <documentation> child element
        NodeList documentNodes = element.getElementsByTagName("documentation");
        if (documentNodes.getLength() > 0) {
            Element documentElement = (Element) documentNodes.item(0);
            return documentElement.getTextContent().trim();
        }
        return null;
    }

    
    public static List<Pair<String, String>> getElementProperties(Document archiMateModel, Element element) {
        List<Pair<String, String>> propertyList = new ArrayList<>();
        NodeList properties = element.getElementsByTagName("property");

        for (int i = 0; i < properties.getLength(); i++) {
            Element property = (Element) properties.item(i);
            String propertyDefinitionRef = property.getAttribute("propertyDefinitionRef");
            String propertyName = getPropertyName(archiMateModel, propertyDefinitionRef);
            String propertyValue = property.getElementsByTagName("value").item(0).getTextContent().trim();
            propertyList.add(new Pair<>(propertyName, propertyValue));
        }
        return propertyList;
    }


    public static String getPropertyName(Document archiMateModel, String propertyDefinitionRef) {
        NodeList propertyDefinitions = archiMateModel.getElementsByTagName("propertyDefinition");
        for (int i = 0; i < propertyDefinitions.getLength(); i++) {
            Element propertyDefinition = (Element) propertyDefinitions.item(i);
            String identifier = propertyDefinition.getAttribute("identifier");
            if (identifier.equals(propertyDefinitionRef)) {
                return propertyDefinition.getElementsByTagName("name").item(0).getTextContent();
            }
        }
        return null;
    }

    public static List<String> returnAllIncomingRelationships(Document archiMateModel, Element concept) {
        List<String> returnList = new ArrayList<>();
        NodeList realtionList = archiMateModel.getElementsByTagName("relationship");
        for (int i = 0; i < realtionList.getLength(); i++) {
            Element relationship = (Element) realtionList.item(i);
            if (relationship.getAttribute("target").equals(concept.getAttribute("identifier"))) {
                returnList.add(
                        findElementById(archiMateModel, relationship.getAttribute("source")).getAttribute("xsi:type"));
            }
        }
        return returnList;
    }

    public static List<Element> returnAllIncomingElements(Document archiMateModel, Element concept) {
        List<Element> returnList = new ArrayList<>();
        NodeList realtionList = archiMateModel.getElementsByTagName("relationship");
        for (int i = 0; i < realtionList.getLength(); i++) {
            Element relationship = (Element) realtionList.item(i);
            if (relationship.getAttribute("target").equals(concept.getAttribute("identifier"))) {
                returnList.add(findElementById(archiMateModel, relationship.getAttribute("source")));
            }
        }
        return returnList;
    }

    public static List<Element> returnAllIncomingElementsPoolLaneOnly(Document archiMateModel, Element concept) {
        List<Element> returnList = new ArrayList<>();
        NodeList realtionList = archiMateModel.getElementsByTagName("relationship");
        for (int i = 0; i < realtionList.getLength(); i++) {
            Element relationship = (Element) realtionList.item(i);
            if (relationship.getAttribute("target").equals(concept.getAttribute("identifier"))) {
                Element tmpElement = findElementById(archiMateModel, relationship.getAttribute("source"));
                String tmpElementType = tmpElement.getAttribute("xsi:type");
                if(tmpElementType.equals("BusinessRole") || tmpElementType.equals("Location") || tmpElementType.equals("BusinessFunction")) {
                    returnList.add(tmpElement);
                }
                
            }
        }
        return returnList;
    }

    public static List<String> returnAllOutgoingRelationships(Document archiMateModel, Element concept) {
        List<String> returnList = new ArrayList<>();
        NodeList realtionList = archiMateModel.getElementsByTagName("relationship");
        for (int i = 0; i < realtionList.getLength(); i++) {
            Element relationship = (Element) realtionList.item(i);
            if (relationship.getAttribute("target").equals(concept.getAttribute("identifier"))) {
                returnList.add(
                        findElementById(archiMateModel, relationship.getAttribute("target")).getAttribute("xsi:type"));
            }
        }
        return returnList;
    }

    // Return all element-ids of given type
    public static List<String> returnAllElementsOfGivenType(Document archiMateModel, String type) {
        List<String> returnList = new ArrayList<>();
        NodeList elementList = archiMateModel.getElementsByTagName("element");
        for (int i = 0; i < elementList.getLength(); i++) {
            Element element = (Element) elementList.item(i);
            if (element.getAttribute("xsi:type").equals(type)) {
                returnList.add(element.getAttribute("identifier"));
            }
        }
        return returnList;
    }


    // Return all Triggering Relationships - but not the ones for the pool or lane itself
    public static List<Element> returnAllRelationshipsOfType(Document archiMateModel, String type) {
        List<Element> returnList = new ArrayList<>();
        NodeList realtionList = archiMateModel.getElementsByTagName("relationship");
        for (int i = 0; i < realtionList.getLength(); i++) {
            Element relationship = (Element) realtionList.item(i);
            if (relationship.getAttribute("xsi:type").equals(type)) {
                Element source = findElementById(archiMateModel, relationship.getAttribute("source"));
                Element target = findElementById(archiMateModel, relationship.getAttribute("target"));

                if(!source.getAttribute("xsi:type").equals("Location") && !target.getAttribute("xsi:type").equals("Location") &&
                    !source.getAttribute("xsi:type").equals("BusinessRole") && !target.getAttribute("xsi:type").equals("BusinessRole")) {
                        returnList.add(relationship);
                }
            }
        }
        return returnList;
    }


    // Remove the leading id- from the ArchiMate identifier
    public static String removePrefix(String input) {
        if (input.startsWith("id-")) {
            return input.substring(3);
        } else {
            return input;
        }
    }

    public static Element getLocationOfBusinessRole(Document archiMateModel, Element businessRoleElement) {
        String businessRoleId = businessRoleElement.getAttribute("identifier");
        NodeList relationshipList = archiMateModel.getElementsByTagName("relationship");
    
        for (int i = 0; i < relationshipList.getLength(); i++) {
            Element relationship = (Element) relationshipList.item(i);
    
            String targetId = relationship.getAttribute("target");
            if (businessRoleId.equals(targetId)) {
                String sourceId = relationship.getAttribute("source");

                Element sourceElement = findElementById(archiMateModel, sourceId);
    
                if (sourceElement != null && "Location".equals(sourceElement.getAttribute("xsi:type"))) {
                    return sourceElement;
                }
            }
        }
        return null;
    }


    public static Map<String, Double> getNodeDimensions(Document archiMateModel, Element element) {
        Map<String, Double> dimensions = new HashMap<>();
        String elementId = element.getAttribute("identifier");
        NodeList nodes = archiMateModel.getElementsByTagName("node");
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            if (node.getAttribute("elementRef").equals(elementId)) {
                dimensions.put("x", Double.parseDouble(node.getAttribute("x")));
                dimensions.put("y", Double.parseDouble(node.getAttribute("y")));
                dimensions.put("w", Double.parseDouble(node.getAttribute("w")));
                dimensions.put("h", Double.parseDouble(node.getAttribute("h")));
                return dimensions;
            }
        }
        return dimensions;
    }


    public static String findBPMNIdByArchiMateId(List<Pair<String, String>> mappings, String archiMateId) {
        for (Pair<String, String> mapping : mappings) {
            if (mapping.getValue0().equals(archiMateId)) {
                return mapping.getValue1();
            }
        }
        return null;
    }




}
