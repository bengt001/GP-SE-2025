package de.techfak.gse.template.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Konfiguriert den PasswordEncoder, der fürs Hashen und Verifizieren von Passwörtern genutzt wird.
 * Encoder kennt mehrer Formate und wählt anhand des Präfixes den passenden.
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
