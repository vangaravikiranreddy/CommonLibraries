package com.security.security_library.config;


import com.security.security_library.filter.CustomAuthenticationFilter;
import com.security.security_library.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilterConfig {
    private final AuthenticationManager authenticationManager;

    @Value("#{${app.request-role-map}}")
    private final Map<String, String> requestRoleMap;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            /*    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v2").hasRole("ADMIN")
                )*/
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customAuthorizationFilter(), ExceptionTranslationFilter.class)
                .build();
    }

    private CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter("/**");
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    private CustomAuthorizationFilter customAuthorizationFilter() {
        return new CustomAuthorizationFilter(requestRoleMap);
    }
}
