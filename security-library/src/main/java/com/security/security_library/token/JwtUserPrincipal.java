package com.security.security_library.token;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtUserPrincipal {
    private String username;
    private String email;
    private String userId;
    private String token;
    private Claims claims;

    public String getName() {
        return username;
    }

    @SuppressWarnings("unchecked")
    public <T> T getClaim(String claimName, Class<T> clazz) {
        return claims.get(claimName, clazz);
    }

    public Object getClaim(String claimName) {
        return claims.get(claimName);
    }

    public boolean hasClaim(String claimName) {
        return claims.containsKey(claimName);
    }
}
