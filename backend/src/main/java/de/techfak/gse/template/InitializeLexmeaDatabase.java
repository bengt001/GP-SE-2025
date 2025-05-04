package de.techfak.gse.template;

import de.techfak.gse.template.parsingUtils.ParsingPipeline;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class InitializeLexmeaDatabase implements InitializingBean {


    public InitializeLexmeaDatabase(){}

    @Override
    public void afterPropertiesSet() {
        try (InputStream is = TemplateApplication.class.getClassLoader().getResourceAsStream("schemasWithFoLTree.json")) {
            assert is != null;
            ParsingPipeline.importLexmeaToDatabase(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
