package com.example.http_client_library.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "http.client")
@Data
public class HttpClientProperties {

    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration readTimeout = Duration.ofSeconds(30);
    private Duration writeTimeout = Duration.ofSeconds(30);
    private int maxConnections = 100;
    private int maxConnectionsPerRoute = 20;
    private boolean enableRetry = true;
    private int maxRetryAttempts = 3;
    private Duration retryDelay = Duration.ofSeconds(1);
    private boolean enableCircuitBreaker = false;
    private String userAgent = "HttpClientLibrary/1.0";
    private Map<String, String> defaultHeaders = new HashMap<>();
}
