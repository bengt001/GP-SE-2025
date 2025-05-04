package de.techfak.gse.template.parsingUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParsingPipeline {


    public static void importLexmeaToDatabase(InputStream is) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            ObjectMapper mapper = new ObjectMapper();
            String jsonTxt = br.lines().collect(Collectors.joining());
            JsonNode root = mapper.readTree(jsonTxt);



            if (!root.isEmpty()) {
                ArrayList<Object> recursionPath = new ArrayList<>();
                ArrayList<ArrayList<Object>> allPaths = new ArrayList<>();
                JsonParser.findPathWithFieldAndValue(root, "type", "schema", recursionPath, allPaths);
                //System.out.println("##########################################################################################");
                for (ArrayList<Object> path : allPaths) {
                    createDeckWithCards(path, root);
                }
            } else {
                //Error
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDeckWithCards(ArrayList<Object> path, JsonNode root) {
        ArrayList<String> rechtgebietList = JsonParser.extractContentAsStringFromPath(root, path, "name");
        ArrayList<String> schemaHtml = JsonParser.extractContentAsStringFromPath(root, path, "text");
        if (rechtgebietList.isEmpty() || schemaHtml.isEmpty()) {
            //Error muss aber nich umbedingt den ganzen prozess abrechen
        } else {
            //Build deck and cards
            //first build cards if there are no cards no deck is build
            ArrayList<String[]> problemBoxes = HtmlParser.getProblemBoxes(schemaHtml.getFirst());
            ArrayList<String[]> definitionBoxes = HtmlParser.getDefinitionBoxes(schemaHtml.getFirst());
            TreeNode<String> aufdeckCard = HtmlParser.getTableOfContentsAsTree(schemaHtml.getFirst());
            if (problemBoxes.isEmpty() && definitionBoxes.isEmpty() && aufdeckCard.hasOtherData()) {
                //Error no cards could be generated
            } else {
                //build Deck with rechtgebietList
                //Merge is needed to complete
            }

        }
    }

    private static void printAllPaths(ArrayList<ArrayList<Object>> allPaths) {
        for (ArrayList<Object> path : allPaths) {
            for (Object obj : path) {
                System.out.print(obj.toString() + " | ");
            }
            System.out.println();
        }
    }
}
