package de.techfak.gse.template;

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

/**
 * Service that initializes the Lexmea database.
 */
@Service
public class InitializeLexmeaDatabase implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeLexmeaDatabase.class);

    private final CardService cardService;
    private final DeckService deckService;
    private final UserService userService;

    private ParsingPipeline pipeline;

    /**
     * Initializes the Lexmea database with provided services.
     *
     * @param cardService the service handling card operations
     * @param deckService the service handling deck operations
     */
    @Autowired
    public InitializeLexmeaDatabase(final CardService cardService, final DeckService deckService,
                                    final UserService userService) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.userService = userService;
        this.pipeline = new ParsingPipeline(this.cardService, this.deckService);
    }

    @SuppressWarnings({"checkstyle:TrailingComment", "checkstyle:MultipleStringLiterals",
            "checkstyle:LocalVariableName"})
    @Override
    public void afterPropertiesSet() {
        try {
            userService.loadUserByUsername("phoenix.wright@aa.de");
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
            Usr user3 = userService.createUser("Clara Ley",
                    "clara@aa.de",
                    "password",
                    "Clara Ley",
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
            deckService.getNewUserDeck(user1, 1);
        }


    }
}
