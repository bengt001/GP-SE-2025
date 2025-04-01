package de.techfak.gse.template;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TemplateApplication.class)
public class TemplateApplicationIT {

    @Test
    public void contextLoads() {
        //Tests Application-Start.
    }

}
