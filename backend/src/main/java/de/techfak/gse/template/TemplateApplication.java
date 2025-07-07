package de.techfak.gse.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot application entry point.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableScheduling
public class TemplateApplication {
    protected TemplateApplication() {
    }
    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

}
