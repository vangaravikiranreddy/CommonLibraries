package com.example.http_client_library.config;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(HttpClientProperties.class)
@RequiredArgsConstructor
public class HttpClientAutoConfiguration {

    private final HttpClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(properties.getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(properties.getMaxConnectionsPerRoute());
        return connectionManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(properties.getConnectTimeout().toMillis()))
                .setResponseTimeout(Timeout.ofMilliseconds(properties.getReadTimeout().toMillis()))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClient httpClient(PoolingHttpClientConnectionManager connectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        return new RestTemplate(factory);
    }
}
