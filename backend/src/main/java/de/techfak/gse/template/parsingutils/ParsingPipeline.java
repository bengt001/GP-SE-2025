package de.techfak.gse.template.parsingutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.techfak.gse.template.domain.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;
/**
 * Pipeline for parsing operations.
 */
public class ParsingPipeline {

    private final CardService cardService;
    private final DeckService deckService;


    public ParsingPipeline(CardService cardService, DeckService deckService) {
        this.cardService = cardService;
        this.deckService = deckService;
    }
    /**
     * Imports Lexmea data into the database from a JSON input stream.
     *
     * @param is the input stream containing the JSON data
     */
    public void importLexmeaToDatabase(InputStream is) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            ObjectMapper mapper = new ObjectMapper();
            String jsonTxt = br.lines().collect(Collectors.joining());
            JsonNode root = mapper.readTree(jsonTxt);


            if (!root.isEmpty()) {
                ArrayList<Object> recursionPath = new ArrayList<>();
                ArrayList<ArrayList<Object>> allPaths = new ArrayList<>();
                JsonParser.findPathWithFieldAndValue(root, "type", "schema", recursionPath, allPaths);
                System.out.println(allPaths.size());
                //System.out.println("#######################################################
                // ###################################");
                for (ArrayList<Object> path : allPaths) {
                    createDeckWithCards(path, root);
                }
            } //else {
                //Error
           // }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDeckWithCards(ArrayList<Object> path, JsonNode root) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<String> rechtgebietList = JsonParser.extractContentAsStringFromPath(root, path, "name");
        ArrayList<String> schemaHtml = JsonParser.extractContentAsStringFromPath(root, path, "text");
        schemaHtml.set(0, schemaHtml.get(0).replace("\n", "\\n"));
        //System.out.println(schemaHtml.getFirst());
        if (rechtgebietList.isEmpty() || schemaHtml.isEmpty()) {
            System.out.println('E');
            //Error muss aber nich umbedingt den ganzen prozess abrechen
        } else {
            //Build deck and cards
            //first build cards if there are no cards no deck is build
            ArrayList<String[]> problemBoxes = HtmlParser.getProblemBoxes(schemaHtml.getFirst());
            ArrayList<String[]> definitionBoxes = HtmlParser.getDefinitionBoxes(schemaHtml.getFirst());
            TreeNode<String> aufdeckCard = HtmlParser.getTableOfContentsAsTree(schemaHtml.getFirst());
            if (problemBoxes.isEmpty() && definitionBoxes.isEmpty() && !aufdeckCard.hasOtherData()) {
                //Error no cards could be generated
                System.out.println("lul");
            } else {
                Deck currentDeck = deckService.addDeck(true, rechtgebietList);
                for (String[] content : problemBoxes) {
                    try {
                        String jsonArray = mapper.writeValueAsString(content);
                        //System.out.println(jsonArray);
                        cardService.addCard(jsonArray, "Probleme", currentDeck);
                    } catch (JsonProcessingException e) {
                        //more like a single card failed what do we do then?
                        // deck couldnt be created exeption
                        throw new RuntimeException(e);
                    }
                }
                for (String[] content : definitionBoxes) {
                    try {
                        String jsonArray = mapper.writeValueAsString(content);
                        //System.out.println(jsonArray);
                        cardService.addCard(jsonArray, "Definitionen", currentDeck);
                    } catch (JsonProcessingException e) {
                        //more like a single card failed what do we do then?
                        // deck couldnt be created exeption
                        throw new RuntimeException(e);
                    }
                }

                try {
                    String jsonArray = mapper.writeValueAsString(aufdeckCard);
                    System.out.println(jsonArray);
                    if (aufdeckCard.hasOtherData()) {
                        //one of these is broken
                        //System.out.println(jsonArray);
                        cardService.addCard(jsonArray, "Aufdeckkarte", currentDeck);
                    }

                } catch (JsonProcessingException e) {
                    //more like a single card failed what do we do then?
                    // deck couldnt be created exeption
                    throw new RuntimeException(e);
                }

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
