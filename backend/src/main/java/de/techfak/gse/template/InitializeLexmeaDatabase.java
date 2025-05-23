package de.techfak.gse.template;

import de.techfak.gse.template.domain.CardService;
import de.techfak.gse.template.domain.DeckService;
import de.techfak.gse.template.parsingutils.ParsingPipeline;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
/**
 * Service that initializes the Lexmea database
 */
@Service
public class InitializeLexmeaDatabase implements InitializingBean {

    private final CardService cardService;
    private final DeckService deckService;

    private ParsingPipeline pipeline;
    /**
     * Initializes the Lexmea database with provided services.
     *
     * @param cardService   the service handling card operations
     * @param deckService   the service handling deck operations
     */
    @Autowired
    public InitializeLexmeaDatabase(final CardService cardService, final DeckService deckService) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.pipeline = new ParsingPipeline(this.cardService, this.deckService);
    }

    @Override
    public void afterPropertiesSet() {
        try (InputStream is = TemplateApplication.class.getClassLoader().
                getResourceAsStream("schemasWithFoLTree.json")) {
            assert is != null;
            pipeline.importLexmeaToDatabase(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
