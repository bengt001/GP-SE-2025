package de.techfak.gse.template.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Ermöglicht die die Annotationen zur Sicherung der Endpunkte in den Methoden des Controllers.
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {

}

