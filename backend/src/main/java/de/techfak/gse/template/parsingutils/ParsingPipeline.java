package de.techfak.gse.template.parsingutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.techfak.gse.template.domain.entities.Deck;
import de.techfak.gse.template.domain.service.CardService;
import de.techfak.gse.template.domain.service.DeckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ParsingPipeline.class);
    private static final String ERROR_MSG = "Deck couldnt be created";

    private int card1 = 0;
    private int card2 = 0;
    private int card3 = 0;

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
    public void importLexmeaToDatabase(InputStream is, int userId) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            ObjectMapper mapper = new ObjectMapper();
            String jsonTxt = br.lines().collect(Collectors.joining());
            JsonNode root = mapper.readTree(jsonTxt);


            if (!root.isEmpty()) {
                ArrayList<Object> recursionPath = new ArrayList<>();
                ArrayList<ArrayList<Object>> allPaths = new ArrayList<>();
                JsonParser.findPathWithFieldAndValue(root, "type", "schema", recursionPath, allPaths);
                LOGGER.debug("allPathsSize: {}", allPaths.size());
                for (ArrayList<Object> path : allPaths) {
                    try {
                        createDeckWithCards(path, root, userId);
                    } catch (DeckCreationFailedException e) {
                        LOGGER.debug("Deck failed to build");
                    }
                }
            } else {
                LOGGER.debug("empty root");
            }
            System.out.println("AMOUNT OF CARDS1: " + card1 + "####################");
            System.out.println("AMOUNT OF CARDS2: " + card2 + "####################");
            System.out.println("AMOUNT OF CARDS3: " + card3 + "####################");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDeckWithCards(ArrayList<Object> path, JsonNode root, int userId)
            throws DeckCreationFailedException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<String> rechtgebietList = JsonParser.extractContentAsStringFromPath(root, path, "name");
        ArrayList<String> schemaHtml = JsonParser.extractContentAsStringFromPath(root, path, "text");
        ArrayList<String> title = JsonParser.extractContentAsStringFromPath(root, path, "title");
        if (rechtgebietList.isEmpty() || schemaHtml.isEmpty()) {
            LOGGER.error("no Content");
            throw new DeckCreationFailedException(ERROR_MSG);
        } else {
            //Build deck and cards
            //first build cards if there are no cards no deck is build
            schemaHtml.set(0, schemaHtml.get(0).replace("\n", "\\n"));
            ArrayList<String[]> problemBoxes = HtmlParser.getProblemBoxes(schemaHtml.getFirst());
            ArrayList<String[]> definitionBoxes = HtmlParser.getDefinitionBoxes(schemaHtml.getFirst());
            TreeNode<String> aufdeckCard = HtmlParser.getTableOfContentsAsTree(schemaHtml.getFirst());
            if (problemBoxes.isEmpty() && definitionBoxes.isEmpty() && !aufdeckCard.hasOtherData()) {
                LOGGER.error("no cards could be generated");
                throw new DeckCreationFailedException(ERROR_MSG);
            } else {
                Deck currentDeck = deckService.addDeck(true, rechtgebietList, userId);

                for (String[] content : problemBoxes) {
                    card1++;
                    try {
                        String jsonArray = mapper.writeValueAsString(content);
                        LOGGER.debug(jsonArray);
                        cardService.addCard(jsonArray, "Probleme", currentDeck, title.getFirst());
                    } catch (JsonProcessingException e) {
                        LOGGER.error("JsonProcessingException: Probleme card fail");
                        throw new RuntimeException(e);
                    }
                }
                for (String[] content : definitionBoxes) {
                    try {
                        card2++;
                        String jsonArray = mapper.writeValueAsString(content);
                        LOGGER.debug(jsonArray);
                        cardService.addCard(jsonArray, "Definitionen", currentDeck, title.getFirst());
                    } catch (JsonProcessingException e) {
                        LOGGER.error("JsonProcessingException: Definitionen card fail");
                        throw new RuntimeException(e);
                    }
                }
                try {
                    String jsonArray = mapper.writeValueAsString(aufdeckCard);
                    System.out.println(jsonArray);
                    if (aufdeckCard.hasOtherData()) {
                        card3++;
                        LOGGER.debug(jsonArray);
                        cardService.addCard(jsonArray, "Aufdeckkarte", currentDeck, title.getFirst());
                    } else {
                        LOGGER.debug("Tree structure doesnt have enough content to build a card");
                    }

                } catch (JsonProcessingException e) {
                    LOGGER.error("JsonProcessingException: Aufdeckkarte card fail");
                    throw new RuntimeException(e);
                }
                if (cardService.getCardsByDeckId(currentDeck.getDeckId()).isEmpty()) {
                    System.out.println("#######################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################");
                    deckService.deleteDeck(currentDeck.getDeckId());
                }

            }

        }
    }

    private static void logAllPaths(ArrayList<ArrayList<Object>> allPaths) {
        for (ArrayList<Object> path : allPaths) {
            for (Object obj : path) {
                LOGGER.debug("{} | ", obj.toString());
            }
        }
    }
}
