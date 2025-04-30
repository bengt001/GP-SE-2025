package de.techfak.gse.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
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
