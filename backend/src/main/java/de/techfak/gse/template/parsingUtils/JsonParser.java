package de.techfak.gse.template.parsingUtils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The type Json parser.
 */
public class JsonParser {

    /**
     * Find path with field and value. This function is recursive recursionPath and allPaths need to be defined before
     *
     * @param jsonContent   the json content
     * @param fieldName     the field name
     * @param value         the value
     * @param recursionPath the recursion path
     * @param allPaths      the all paths
     */
    public static void findPathWithFieldAndValue(JsonNode jsonContent,
                                                 String fieldName,
                                                 String value,
                                                 ArrayList<Object> recursionPath,
                                                 ArrayList<ArrayList<Object>> allPaths) {

        if (jsonContent.has(fieldName)) {
            if (jsonContent.get(fieldName) != null && jsonContent.get(fieldName).asText().equals(value)) {
                allPaths.add(new ArrayList<>(recursionPath));
            }
        }
        if (jsonContent.isContainerNode() && jsonContent.getNodeType() == JsonNodeType.OBJECT) {
            Iterator<String> fieldNames = jsonContent.fieldNames();
            while (fieldNames.hasNext()) {
                String currentName = fieldNames.next();
                JsonNode currentValue = jsonContent.get(currentName);
                //System.out.println("currentName: " + currentName);
                recursionPath.add(recursionPath.size(), currentName);
                findPathWithFieldAndValue(currentValue, fieldName, value, recursionPath, allPaths);
                recursionPath.remove(recursionPath.size() - 1);
            }
        } else {
            for (int i = 0; i < jsonContent.size(); i++) {
                //System.out.println(i + ": " + jsonContent.size());
                JsonNode keyValue = jsonContent.get(i);
                if (keyValue != null) {
                    //System.out.println(i + ": " + jsonContent.size() + ": keyValue not null");
                    if (keyValue.isContainerNode()) {
                        //System.out.println("is ContainerNode");
                        recursionPath.add(recursionPath.size(), i);
                        findPathWithFieldAndValue(keyValue, fieldName, value, recursionPath, allPaths);
                        recursionPath.remove(recursionPath.size() - 1);
                    }
                }
            }
        }
    }


    /**
     * Extract content as string from path array list.
     *
     * @param jsonContent   the json content
     * @param recursionPath the recursion path
     * @param fieldname     the fieldname
     * @return the array list
     */
    public static ArrayList<String> extractContentAsStringFromPath
    (JsonNode jsonContent, ArrayList<Object> recursionPath, String fieldname) {
        ArrayList<String> stringList = new ArrayList<>();
        JsonNode currentNode = jsonContent;
        for (int i = 0; i < recursionPath.size(); i++) {
            if (currentNode == null) {
                break;
            }
            if (recursionPath.get(i) instanceof String) {
                currentNode = currentNode.get((String) recursionPath.get(i));
                if (currentNode.has(fieldname)) {
                    JsonNode nameNode = currentNode.get(fieldname);
                    stringList.add(nameNode.asText());
                    //System.out.print(nameNode.toString());
                }
            } else if (recursionPath.get(i) instanceof Integer) {
                currentNode = currentNode.get((Integer) recursionPath.get(i));
                if (currentNode.has(fieldname)) {
                    JsonNode nameNode = currentNode.get(fieldname);
                    stringList.add(nameNode.asText());
                    //System.out.print(nameNode.toString());
                }
            } else {
                //throw new IllegalArgumentException
                System.out.println("Path element must be String or Integer");
            }

        }
        return stringList;
    }
}
