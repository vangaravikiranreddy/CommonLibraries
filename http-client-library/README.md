# HTTP Client Library

A Spring Boot auto-configuration library that provides a standardized HTTP client implementation using Apache HttpComponents Client 5. This library offers configurable connection management, timeouts, and retry mechanisms for consistent HTTP communication across all services.

## Overview

This library automatically configures a `RestTemplate` with Apache HttpClient 5 backend, providing enterprise-grade HTTP client capabilities with sensible defaults and extensive customization options through Spring Boot configuration properties.

## Features

- **Auto-configuration**: Automatic Spring Boot configuration with `@ConditionalOnMissingBean`
- **Connection Pooling**: Configurable connection pool management
- **Timeout Configuration**: Separate connect and read timeout settings
- **Retry Mechanisms**: Built-in retry logic with configurable attempts and delays
- **Circuit Breaker Support**: Optional circuit breaker pattern implementation
- **Custom Headers**: Default headers configuration
- **Spring Boot Integration**: Seamless integration with Spring Boot applications

## Requirements

- Java 17+
- Spring Boot 3.5.4+
- Apache HttpComponents Client 5

## Installation

### Maven
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>http-client-library</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
## Quick Start

### Basic Usage

The library auto-configures a `RestTemplate` bean that you can inject into your services:

```java
@Service
public class ApiService {
    
    private final RestTemplate restTemplate;
    
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public String callExternalApi() {
        return restTemplate.getForObject("https://api.example.com/data", String.class);
    }
}
```

### Configuration

Configure the HTTP client through `application.yml` or `application.properties`:

#### application.yml
```yaml
http:
  client:
    connect-timeout: 10s
    read-timeout: 30s
    write-timeout: 30s
    max-connections: 100
    max-connections-per-route: 20
    enable-retry: true
    max-retry-attempts: 3
    retry-delay: 1s
    enable-circuit-breaker: false
    user-agent: "MyApp/1.0"
    default-headers:
      Content-Type: "application/json"
      Accept: "application/json"
```

#### application.properties
```properties
http.client.connect-timeout=10s
http.client.read-timeout=30s
http.client.write-timeout=30s
http.client.max-connections=100
http.client.max-connections-per-route=20
http.client.enable-retry=true
http.client.max-retry-attempts=3
http.client.retry-delay=1s
http.client.enable-circuit-breaker=false
http.client.user-agent=MyApp/1.0
http.client.default-headers.Content-Type=application/json
http.client.default-headers.Accept=application/json
```

## Advanced Usage

### Custom HttpClient Configuration

You can override any of the auto-configured beans by defining your own:

```java
@Configuration
public class CustomHttpClientConfig {
    
    @Bean
    @Primary
    public HttpClient customHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(5))
                        .build())
                .build();
    }
}
```

### Multiple RestTemplate Instances

For different endpoints with different configurations:

```java
@Configuration
public class MultipleRestTemplateConfig {
    
    @Bean
    @Qualifier("sample")
    public RestTemplate fastApiRestTemplate() {
        HttpClient fastClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(2))
                        .setResponseTimeout(Timeout.ofSeconds(5))
                        .build())
                .build();
        
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(fastClient);
        return new RestTemplate(factory);
    }
}
```

## Testing

The library includes test configurations for easy testing:

```java
@SpringBootTest
class HttpClientLibraryTest {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Test
    void testHttpClientConfiguration() {
        assertThat(restTemplate).isNotNull();
        // Add your tests here
    }
}
```

## Contributing

When contributing to this library:

1. Follow Java coding conventions and Spring Boot best practices
2. Ensure backward compatibility for configuration properties
3. Add unit and integration tests for new features
4. Update this README with any new configuration options
5. Follow semantic versioning for releases

## Dependencies

This library uses the following key dependencies:

- **Spring Boot Starter Web**: Core Spring web functionality
- **Apache HttpComponents Client 5**: Modern HTTP client implementation
- **Lombok**: Reduces boilerplate code
- **Spring Boot Configuration Processor**: Generates metadata for configuration properties

See `pom.xml` for complete dependency list and versions. The library is compatible with Java 17+ and Spring Boot 3.5.4+.

## Troubleshooting

### Common Issues

**Connection Pool Exhaustion**
- Increase `max-connections` or `max-connections-per-route`
- Ensure proper connection cleanup in your code

**Timeout Issues**
- Adjust `connect-timeout` and `read-timeout` based on your API requirements
- Consider enabling retry mechanism for transient failures

**Memory Issues**
- Monitor connection pool metrics
- Ensure proper resource cleanup

### Logging

Enable debug logging to troubleshoot HTTP client issues:

```yaml
logging:
  level:
    org.apache.hc.client5: DEBUG
```

---

**Note**: This library is designed for internal use across our service ecosystem. Ensure proper configuration and testing when integrating into new services.