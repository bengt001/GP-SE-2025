package de.techfak.gse.template.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Klasse zum Konfigurieren verschiedener Einstellungen die die Erreichbarkeit des Servers
 * und das Verhalten dieses veränder können.
 */
@Configuration
@EnableConfigurationProperties(SecurityConstants.class)
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final SecurityConstants securityConstants;
    private final AuthenticationConfiguration configuration;

    /**
     * Konstuktor für die Security Konfigration.
     * @param userDetailsService Dienst zum Laden von Benutzerdetails für Authentifizierung und Autorisierung.
     * @param securityConstants sicherheitsrelevante Konstanten.
     * @param configuration Authentifizierungskonfiguration,
     *                      die Details für die Authentifizierung und Autorisierung enthält.
     */
    @Autowired
    public SecurityConfig(final UserDetailsService userDetailsService, final SecurityConstants securityConstants,
                          final AuthenticationConfiguration configuration) {
        super();
        this.userDetailsService = userDetailsService;
        this.securityConstants = securityConstants;
        this.configuration = configuration;
    }

    /**
     * Deaktiviert CSRF da der Schutz mot JWT ausreichend ist. CSRF würden nur zu mehr trffic führen.
     * @param http die HttpSecurity-Instanz, die verwendet wird, um die Sicherheitskonfiguration zu definieren
     * @return eine konfigurierte SecurityFilterChain-Instanz, die Sicherheitsfilter und -regeln enthält
     * @throws Exception Exception falls ein Fehler bei der Konfiguration der Sicherheitsfilter auftritt
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz ->
                        authz.requestMatchers(HttpMethod.GET).permitAll()
                                .requestMatchers("/api/**").authenticated()
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityConstants))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, securityConstants))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

}

