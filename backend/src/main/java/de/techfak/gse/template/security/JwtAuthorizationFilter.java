package de.techfak.gse.template.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

/**
 * Filtert alle Requests und versucht Authentifizierungs-Daten zu extrahieren.
 * Sind keine Vorhanden wird an den nächsten Filter übergeben.
 * Andernfalls werden die Daten an den SecrityContext übergeben , damit Controller auf den Nutzer ugreifen kann.
 */
public final class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final UserDetailsService userDetailsService;
    private final SecurityConstants securityConstants;

    /**
     * Konstruktor für einen neuen JWT-Autorisierungsfilter
     * @param authenticationManager Der Manager zur Überprüfung von Authentifizierungen.
     * @param userDetailsService Dienst zum Laden von Benutzerdetails anhand von Token-Informationen.
     * @param securityConstants Enthält für Spring Security notwendigen Konstanten.
     */
    public JwtAuthorizationFilter(final AuthenticationManager authenticationManager,
                                  final UserDetailsService userDetailsService,
                                  final SecurityConstants securityConstants) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.securityConstants = securityConstants;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        String token = request.getHeader(securityConstants.getTokenHeader());
        if (token != null && !token.isEmpty() && token.startsWith(securityConstants.getTokenPrefix())) {
            try {
                byte[] signingKey = securityConstants.getJwtSecret().getBytes();
                Jws<Claims> parsedToken = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(signingKey)).build()
                        .parseSignedClaims(token.replace(securityConstants.getTokenPrefix(), "").strip());

                String username = parsedToken.getPayload().getSubject();

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (username != null && !username.isEmpty()) {
                    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                }
            } catch (final ExpiredJwtException exception) {
                // Das kann gerne mal in der Projektentwicklung
                // und auch in der fertigen Anwendung passieren, also überlegt euch, wie ihr damit umgeht!
                LOG.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            } catch (final UnsupportedJwtException exception) {
                LOG.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            } catch (final MalformedJwtException exception) {
                LOG.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            } catch (final SignatureException exception) {
                LOG.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                LOG.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            }
        }
        return null;
    }
}
