# CommonLibraries

A centralized collection of shared libraries and utilities used across all services in our application ecosystem. This repository provides consistent, reusable components for common functionality like HTTP communication, database operations, messaging, and security.

## Overview

CommonLibraries serves as the foundation layer for all our microservices, ensuring consistency, reducing code duplication, and providing standardized implementations for core functionality.

## Libraries Included

### HTTP Client
**Purpose**: Standardized HTTP communication layer for all external API calls and inter-service communication.

**Features**:
- Configurable timeouts and retry policies
- Request/response logging and monitoring
- Authentication header management
- Error handling and circuit breaker patterns
- Support for multiple content types (JSON, XML, form data)

**Usage**: Import and configure with service-specific settings for consistent API communication patterns.

## Configuration

Each library requires configuration specific to your service needs. See the individual library documentation for detailed configuration options.

## Documentation

- [HTTP Client Documentation](./docs/http-client.md)

## Contributing

When adding new common functionality:

1. Ensure the feature is truly common across multiple services
2. Follow established patterns and conventions
3. Add comprehensive tests
4. Update documentation
5. Bump version appropriately
---

**Note**: This library is designed for internal use across our service ecosystem. Ensure proper configuration and testing when integrating into new services.