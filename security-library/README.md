# Spring Security Custom Authentication Library

A Spring Boot security library that provides custom authentication filtering with JWT support and flexible authentication providers.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Reference](#api-reference)
## 🔍 Overview

This security library provides a custom authentication filter implementation for Spring Boot applications. It includes JWT authentication support, configurable authentication providers, and seamless integration with Spring Security's filter chain.

## ✨ Features

- **Custom Authentication Filter**: Extensible filter for handling various authentication methods
- **JWT Support**: Built-in JWT token validation and processing
- **Flexible Authentication Providers**: Pluggable authentication provider system
- **Spring Security Integration**: Seamless integration with Spring Security filter chain
- **Configurable Endpoints**: Customizable authentication endpoints
- **Success/Failure Handlers**: Built-in authentication result handling

## 🏗️ Architecture

```
┌─────────────────────┐
│   HTTP Request      │
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│ CustomAuthentication│
│      Filter         │
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│ Authentication      │
│     Manager         │
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│ JWT Authentication  │
│     Provider        │
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│   Authenticated     │
│     Response        │
└─────────────────────┘
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Spring Boot 3.x
- Maven or Gradle

### Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.security</groupId>
    <artifactId>security-library</artifactId>
    <version>1.0.0</version>
</dependency>
```

## ⚙️ Configuration

### 1. Configure Authentication Properties

```yaml
# application.yml
security:
  authentication:
    jwt:
      secret-key: your-secret-key
      expiration: 86400000 # 24 hours
    endpoints:
      login: /api/auth/login
      logout: /api/auth/logout
```

## 📖 Usage

### Authentication Flow

1. **Client sends request** with authentication credentials
2. **CustomAuthenticationFilter** intercepts the request
3. **AuthenticationManager** processes the authentication
4. **JwtAuthenticationProvider** validates the credentials
5. **Authentication result** is returned to client

### Making Authenticated Requests

#### Authenticated Request
```bash
curl -X GET http://localhost:8080/api/protected \
  -H "Authorization: Bearer your-jwt-token"
```

## 📚 API Reference

### Classes Overview

| Class | Description |
|-------|-------------|
| `CustomAuthenticationFilter` | Main authentication filter that intercepts requests |
| `JwtAuthenticationProvider` | Handles JWT token validation and authentication |
| `AuthenticationConfig` | Configuration for authentication manager |
| `SecurityFilterConfig` | Security filter chain configuration |

### CustomAuthenticationFilter

**Constructor**
```java
public CustomAuthenticationFilter(String defaultFilterProcessesUrl)
```

**Key Methods**
- `attemptAuthentication()`: Processes authentication attempt
- `setAuthenticationManager()`: Sets the authentication manager
- `setAuthenticationSuccessHandler()`: Sets success handler
- `setAuthenticationFailureHandler()`: Sets failure handler

### JwtAuthenticationProvider

**Key Methods**
- `authenticate(Authentication authentication)`: Validates authentication
- `supports(Class<?> authentication)`: Checks if provider supports authentication type


### v1.0.0
- Initial release
- Basic JWT authentication support
- Custom filter implementation
- Spring Security integration

---

