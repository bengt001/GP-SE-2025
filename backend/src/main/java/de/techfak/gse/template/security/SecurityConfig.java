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

@Configuration
@EnableConfigurationProperties(SecurityConstants.class)
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final SecurityConstants securityConstants;
    private final AuthenticationConfiguration configuration;

    @Autowired
    public SecurityConfig(final UserDetailsService userDetailsService, final SecurityConstants securityConstants,
                          final AuthenticationConfiguration configuration) {
        super();
        this.userDetailsService = userDetailsService;
        this.securityConstants = securityConstants;
        this.configuration = configuration;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception { //<1>

        return http.csrf(AbstractHttpConfigurer::disable) //<2>
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //<3>
                .authorizeHttpRequests(authz -> //<4>
                        authz.requestMatchers(HttpMethod.GET).permitAll() //<4.1>
                                .requestMatchers("/api/**").permitAll()) //<4.2>
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityConstants)) //<5>
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, securityConstants))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

}

