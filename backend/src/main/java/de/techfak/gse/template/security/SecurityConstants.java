package de.techfak.gse.template.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Klasse Security Constants definiert Konstanten, die für Spring Security benötigt werden.
 */
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

    /**
     * Konstruktur setzt die Konstanten sie für Spring Security benötigt werden.
     * @param authLoginUrl Url an die die Zugangsdaten gesendet werden.
     * @param jwtSecret Server Geheimnis für Prüfsumme.
     * @param tokenHeader Name des Headers.
     * @param tokenPrefix Präfix mit dem Token gesendet wird und wo er in einem Request abgerufen wird.
     * @param tokenType Typ des Tokens.
     * @param tokenIssuer Aussteller, in diesem Fall die geschützte API.
     * @param tokenAudience Defenieren wer einen Token verarbeiten können soll.
     */
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
