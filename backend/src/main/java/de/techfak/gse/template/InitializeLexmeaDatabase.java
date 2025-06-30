package de.techfak.gse.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.template.domain.Rating;
import de.techfak.gse.template.domain.entities.Card;
import de.techfak.gse.template.domain.entities.CardInfo;
import de.techfak.gse.template.domain.entities.Deck;
import de.techfak.gse.template.domain.implementation.CardInfoCardDTO;
import de.techfak.gse.template.domain.service.CardService;
import de.techfak.gse.template.domain.service.DeckService;
import de.techfak.gse.template.domain.service.UserService;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.parsingutils.ParsingPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service that initializes the Lexmea database.
 */
@Service
public class InitializeLexmeaDatabase implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeLexmeaDatabase.class);

    private final CardService cardService;
    private final DeckService deckService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    private final ParsingPipeline pipeline;

    /**
     * Initializes the Lexmea database with provided services.
     *
     * @param cardService the service handling card operations
     * @param deckService the service handling deck operations
     */
    @Autowired
    public InitializeLexmeaDatabase(final CardService cardService, final DeckService deckService,
                                    final UserService userService, final ObjectMapper objectMapper) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.userService = userService;
        this.pipeline = new ParsingPipeline(this.cardService, this.deckService);
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings({"checkstyle:TrailingComment", "checkstyle:MultipleStringLiterals", "checkstyle:LocalVariableName", "checkstyle:LineLength", "checkstyle:MagicNumber"})
    @Override
    public void afterPropertiesSet() {
        try {
            userService.loadUserByUsername("phoenix.wright@aa.de");
            try {
                Optional<Card> card = deckService.getCardByIdFromDeck(1, 1);
                List<CardInfo> cardInfoList = deckService.getUserCards(userService.loadUserByUsername("phoenix.wright@aa.de"), 1);
                if (card.isPresent()) {
                    System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(card.get()));
                }
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cardInfoList));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (UsernameNotFoundException ex) {
            Usr user1 = userService.createUser("Phoenix Wright",
                    "phoenix.wright@aa.de",
                    "password",
                    "Phoenix Wright",
                    "ROLE_USER"); //brodcaster
            Usr user2 = userService.createUser("Lexmea",
                    "lexmea@law.de",
                    "password",
                    "Lexmea",
                    "ROLE_USER"); //Lexmea
            Usr user3 = userService.createUser("Maya Fey",
                    "maya.fey@aa.de",
                    "password",
                    "Maya Fey",
                    "ROLE_USER");
            try (InputStream is = TemplateApplication.class.getClassLoader().
                    getResourceAsStream("schemasWithFoLTree.json")) {
                assert is != null;
                pipeline.importLexmeaToDatabase(is, Integer.parseInt(user1.getUserId()));
            } catch (IOException e) {
                LOGGER.error("afterPropertiesSet: IOException");
                throw new RuntimeException(e);
            }
            try (InputStream is = TemplateApplication.class.getClassLoader().
                    getResourceAsStream("schemasWithFoLTree.json")) {
                assert is != null;
                pipeline.importLexmeaToDatabase(is, Integer.parseInt(user2.getUserId()));
            } catch (IOException e) {
                LOGGER.error("afterPropertiesSet: IOException");
                throw new RuntimeException(e);
            }
            try (InputStream is = TemplateApplication.class.getClassLoader().
                    getResourceAsStream("schemasWithFoLTree.json")) {
                assert is != null;
                pipeline.importLexmeaToDatabase(is, Integer.parseInt(user3.getUserId()));
            } catch (IOException e) {
                LOGGER.error("afterPropertiesSet: IOException");
                throw new RuntimeException(e);
            }
            Optional<Deck> updatedDeck = deckService.getNewUserDeck(user1, 1);
            if (updatedDeck.isPresent()) {
                Deck tempUpdate = updatedDeck.get();
                tempUpdate.setVisibility(false);
                deckService.updateDeck(user1, 1, tempUpdate);
            }
            deckService.getNewUserDeck(user1, 2);
            List<Deck> decks = deckService.getUserDecks(user1);
            Optional<Deck> deck = deckService.getUserDeckById(user1, 1);
            List<CardInfo> cardInfoList = deckService.getUserCards(user1, 1);
            Optional<Card> card = deckService.getCardByIdFromDeck(1, 1);
            if (card.isPresent()) {
                Card upCard = card.get();
                upCard.setContent("\"{\\\"data\\\":\\\"root\\\",\\\"children\\\":[{\\\"data\\\":\\\"Dear Diary\\\",\\\"children\\\":[{\\\"data\\\":\\\"Erfolgscharakter: Erfolgs- und Tätigkeitsdelikte\\\",\\\"children\\\":[{\\\"data\\\":\\\"Erfolgsdelikte\\\",\\\"children\\\":[]},{\\\"data\\\":\\\"Tätigkeitsdelikte\\\",\\\"children\\\":[]}]},{\\\"data\\\":\\\"Begehungsform: Begehungs- und Unterlassungsdelikte\\\",\\\"children\\\":[{\\\"data\\\":\\\"Begehungsdelikte\\\",\\\"children\\\":[]},{\\\"data\\\":\\\"Unterlassungsdelikte\\\",\\\"children\\\":[]}]},{\\\"data\\\":\\\"Täterkreis: Allgemeindelikte, Sonderdelikte und eigenhändige Delikte\\\",\\\"children\\\":[{\\\"data\\\":\\\"Allgemeindelikte\\\",\\\"children\\\":[]},{\\\"data\\\":\\\"Sonderdelikte\\\",\\\"children\\\":[]},{\\\"data\\\":\\\"Eigenhändige Delikte\\\",\\\"children\\\":[]}]}]}]}\"");
                deckService.updateCard(user1, 1, 1, upCard);
            }
            Optional<CardInfo> cardInfo = deckService.getUseCardById(user1, 1, 1);
            try {
                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(decks);
                System.out.println(json);
                if (deck.isPresent()) {
                    String json2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(deck.get());
                    System.out.println(json2);
                }
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cardInfoList));
                if (card.isPresent()) {
                    System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(card.get()));
                }
                if (cardInfo.isPresent()) {
                    System.out.println(objectMapper.writerWithDefaultPrettyPrinter().
                            writeValueAsString(cardInfo.get()));
                }
                deckService.rankCard(user1, 1, 1, Rating.EASY);

                deckService.getNewUserDeck(user1, 3);
                deckService.getNewUserDeck(user1, 4);
                deckService.getNewUserDeck(user1, 5);
                List<CardInfoCardDTO> cardsAndInfo = deckService.getMaxLearningCards(
                        user1, new long[]{1, 2, 3, 4}, 1000);
                System.out.println("#######################################################");
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().
                        writeValueAsString(cardsAndInfo));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
