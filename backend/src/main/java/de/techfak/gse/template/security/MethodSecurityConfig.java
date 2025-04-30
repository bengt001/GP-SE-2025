package de.techfak.gse.template.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration //<1>
@EnableMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {

}

