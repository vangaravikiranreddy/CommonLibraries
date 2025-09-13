package com.security.security_library.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final RequestMatcher protectedEndpoints;
    private final Map<String, String> requestRoleMap;

    public CustomAuthorizationFilter(Map<String, String> requestRoleMap) {
        this.requestRoleMap = requestRoleMap;
        this.protectedEndpoints = createProtectedEndpoints();
    }

    private RequestMatcher createProtectedEndpoints() {
        PathPatternRequestMatcher.Builder builder = PathPatternRequestMatcher.withDefaults();
        return new OrRequestMatcher(
                builder.matcher("/v1")
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = securityContextHolderStrategy.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.isAuthenticated()) {
            if (protectedEndpoints.matches(request)) {
                if (hasRole(authentication.getAuthorities(), requestRoleMap.get(request.getRequestURI()))) {
                    filterChain.doFilter(request, response);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
        throw new AccessDeniedException("User is not Authorized!!");
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        if (authorities == null || role == null) {
            return false;
        }
        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority().toUpperCase();
            String roleName = role.toUpperCase();

            if (authorityName.equals("ROLE_" + roleName) || authorityName.equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
