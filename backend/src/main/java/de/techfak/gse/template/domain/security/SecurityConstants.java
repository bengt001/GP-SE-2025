package de.techfak.gse.template.domain.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security")
public final class SecurityConstants {

    private final String authLoginUrl;

    // Signing key für den HS512-Algorithmus
    private final String jwtSecret;

    // JWT Token-Standardwerte
    private final String tokenHeader;
    private final String tokenPrefix;
    private final String tokenType;
    private final String tokenIssuer;
    private final String tokenAudience;

    public SecurityConstants(final String authLoginUrl, final String jwtSecret,
                             final String tokenHeader, final String tokenPrefix, final String tokenType,
                             final String tokenIssuer, final String tokenAudience) {
        this.authLoginUrl = authLoginUrl;
        this.jwtSecret = jwtSecret;
        this.tokenHeader = tokenHeader;
        this.tokenPrefix = tokenPrefix;
        this.tokenType = tokenType;
        this.tokenIssuer = tokenIssuer;
        this.tokenAudience = tokenAudience;
    }

    public String getAuthLoginUrl() {
        return authLoginUrl;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public String getTokenAudience() {
        return tokenAudience;
    }
}
