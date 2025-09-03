package com.security.security_library.providers;

import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Log
public class AbstractAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getPrincipal());

        try {
            return parseAndValidateToken(token);
        } catch (Exception e) {
            log.info("Unexpected error during JWT authentication");
            throw new BadCredentialsException("Authentication failed", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    protected Authentication parseAndValidateToken(String token) {
        return null;
    }
}
