package com.security.security_library.providers;

import com.security.security_library.token.JwtAuthenticationToken;
import com.security.security_library.token.JwtUserPrincipal;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider extends AbstractAuthenticationProvider {

    private final PublicKey key;

    public Authentication parseAndValidateToken(String token) {
        try {
            JwtParserBuilder parserBuilder = Jwts.parser()
                    .setSigningKey(key);

            JwtParser parser = parserBuilder.build();

            Jws<Claims> jws = parser.parseClaimsJws(token);
            Claims claims = jws.getBody();

            validateTokenClaims(claims);
            return getAuthentication(claims, token);
        } catch (Exception e) {
            throw new BadCredentialsException("Failed to parse JWT token", e);
        }
    }

    private void validateTokenClaims(Claims claims) throws BadCredentialsException {

        Date expiration = claims.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            throw new BadCredentialsException("Token has expired");
        }

        if (claims.getSubject() == null || claims.getSubject().trim().isEmpty()) {
            throw new BadCredentialsException("Token subject is missing");
        }

        Date issuedAt = claims.getIssuedAt();
        if (issuedAt != null && issuedAt.after(new Date())) {
            throw new BadCredentialsException("Token issued in the future");
        }
    }

    private Authentication getAuthentication(Claims claims, String token) {
        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        String userId = claims.get("userId", String.class);

        Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);

        JwtUserPrincipal principal = JwtUserPrincipal.builder()
                .username(username)
                .email(email)
                .userId(userId)
                .token(token)
                .claims(claims)
                .build();

        return new JwtAuthenticationToken(principal, null, authorities);
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractAuthorities(Claims claims) {
        List<String> roles = null;

        if (claims.containsKey("roles")) {
            roles = claims.get("roles", List.class);
        } else if (claims.containsKey("authorities")) {
            roles = claims.get("authorities", List.class);
        } else if (claims.containsKey("permissions")) {
            roles = claims.get("permissions", List.class);
        } else if (claims.containsKey("scope")) {
            String scopeString = claims.get("scope", String.class);
            if (scopeString != null) {
                roles = List.of(scopeString.split("\\s+"));
            }
        }

        if (roles == null || roles.isEmpty()) {
            log.info("No roles found in token claims, using default USER role");
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return roles.stream()
                .map(role -> {
                    // Ensure role has ROLE_ prefix
                    String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());
    }
}
