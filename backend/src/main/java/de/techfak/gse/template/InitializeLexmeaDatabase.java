package de.techfak.gse.template;

import de.techfak.gse.template.domain.CardService;
import de.techfak.gse.template.domain.DeckService;
import de.techfak.gse.template.parsingUtils.ParsingPipeline;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class InitializeLexmeaDatabase implements InitializingBean {

    private final CardService cardService;
    private final DeckService deckService;

    private ParsingPipeline pipeline;

    @Autowired
    public InitializeLexmeaDatabase(final CardService cardService, final DeckService deckService){
        this.cardService = cardService;
        this.deckService = deckService;
        this.pipeline = new ParsingPipeline(this.cardService, this.deckService);
    }

    @Override
    public void afterPropertiesSet() {
        try (InputStream is = TemplateApplication.class.getClassLoader().getResourceAsStream("schemasWithFoLTree.json")) {
            assert is != null;
            pipeline.importLexmeaToDatabase(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
