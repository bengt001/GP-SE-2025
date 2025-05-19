package de.techfak.gse.template.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter der eine Anfrage zur Authentifizierung bearbeitet.
 * Gibt als Antwort einen JSON-Web Token zurück.
 */
public final class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final int TEN_DAYS_IN_MILLIS = 864_000_000;

    private final AuthenticationManager authenticationManager;

    private final SecurityConstants securityConstants;

    /**
     * Konstrukor für einen neuen JwtAuthentication Filter.
     * @param authenticationManager Manager , der für das Authentifizierten zuständg ist.
     * @param securityConstants Enthält für Spring Security notwendigen Konstanten.
     */
    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager,
                                   final SecurityConstants securityConstants) {
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;
        setFilterProcessesUrl(this.securityConstants.getAuthLoginUrl());
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        //in unserem fall kein username sondern email
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain filterChain, final Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = securityConstants.getJwtSecret().getBytes();
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512)
                .header().add("typ", securityConstants.getTokenType())
                .and()
                .issuer(securityConstants.getTokenIssuer())
                .audience().add(securityConstants.getTokenAudience())
                .and()
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + TEN_DAYS_IN_MILLIS))
                // + 10 Tage
                .claim("rol", roles)
                .compact();

        response.addHeader(securityConstants.getTokenHeader(), securityConstants.getTokenPrefix() + token);
    }
}
