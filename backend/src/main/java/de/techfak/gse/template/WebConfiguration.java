package de.techfak.gse.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfguration klasse die einen WebMvcConfigurrer implementiert und ihn als Bean zur Verfügung stellt.
 * */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * Ertstellt einen neuen WebMvcConfigurer und stellt ihn als Bean zur Verfügung.
     * */
    @Bean
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(final ViewControllerRegistry registry) {
                registry.addViewController("{_:^(?!api|public|assets)[^\\.]*}/**")
                        .setViewName("forward:/");
            }
        };
    }
}
