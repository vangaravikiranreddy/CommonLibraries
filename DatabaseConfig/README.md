# Database Configuration Library

A Spring Boot starter library that provides standardized database configuration with HikariCP connection pooling for microservices architecture.

## Features

- **HikariCP Connection Pooling** - High-performance JDBC connection pool
- **Auto-Configuration** - Spring Boot auto-configuration support
- **Retry Mechanism** - Built-in retry logic for database connection failures
- **Configurable Properties** - Comprehensive database configuration options
- **Production Ready** - Optimized settings for production environments
- **Multi-Service Support** - Reusable across multiple microservices

## Installation

### Maven
```xml
<dependency>
    <groupId>com.database</groupId>
    <artifactId>database-config</artifactId>
    <version>1.0.0</version>
</dependency>
```
## Configuration

Add the following properties to your `application.yml` or `application.properties`:

### YAML Configuration
```yaml
db:
  connection:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
    maximum-pool-size: 10
    minimum-idle: 5
    idle-timeout: 300000
    connection-timeout: 20000
    max-lifetime: 1800000
    keep-alive-time: 120000
    auto-commit: true
    pool-name: "MyApp-Pool"
    initialization-fail-timeout: 1
    isolate-internal-queries: false
    allow-pool-suspension: false
    validation-timeout: 5000
    leak-detection-threshold: 2000
    max-retries: 3
    retry-delay: 3000
    read-only: false
```

### Properties Configuration
```properties
db.connection.url=jdbc:mysql://localhost:3306/your_database
db.connection.username=your_username
db.connection.password=your_password
db.connection.driver-class-name=com.mysql.cj.jdbc.Driver
db.connection.maximum-pool-size=10
db.connection.minimum-idle=5
db.connection.pool-name=MyApp-Pool
```

## Configuration Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `url` | String | - | Database JDBC URL |
| `username` | String | - | Database username |
| `password` | String | - | Database password |
| `driver-class-name` | String | - | JDBC driver class name |
| `maximum-pool-size` | int | 10 | Maximum number of connections in pool |
| `minimum-idle` | int | 5 | Minimum idle connections in pool |
| `idle-timeout` | int | 300000 | Maximum idle time for connections (ms) |
| `connection-timeout` | int | 20000 | Maximum time to wait for connection (ms) |
| `max-lifetime` | int | 1800000 | Maximum lifetime of connection (ms) |
| `keep-alive-time` | long | 120000 | Keep-alive interval (ms) |
| `auto-commit` | boolean | true | Auto-commit behavior |
| `pool-name` | String | "default" | Connection pool name |
| `initialization-fail-timeout` | long | 1 | Pool initialization timeout |
| `isolate-internal-queries` | boolean | false | Isolate internal pool queries |
| `allow-pool-suspension` | boolean | false | Allow pool suspension |
| `validation-timeout` | long | 5000 | Connection validation timeout (ms) |
| `leak-detection-threshold` | long | 2000 | Connection leak detection threshold (ms) |
| `max-retries` | int | 3 | Maximum connection retry attempts |
| `retry-delay` | long | 3000 | Delay between retry attempts (ms) |
| `read-only` | boolean | false | Read-only connection mode |

## Usage

### Basic Usage
Once the library is included and configured, the HikariDataSource will be automatically configured and available for dependency injection:

```java
@Service
public class UserService {
    
    @Autowired
    private DataSource dataSource;
    
    public List<User> getAllUsers() {
        // Use dataSource for database operations
    }
}
```

### With JPA
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // ... getters and setters
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
```

## Connection Pool Behavior

### Startup Behavior
- **Pool Initialization**: Creates `minimum-idle` connections immediately
- **Connection Validation**: Tests initial connection during startup
- **Retry Logic**: Attempts reconnection with configurable retry parameters
- **Failure Handling**: Throws runtime exception if all retries fail

### Runtime Behavior
- **On-Demand Creation**: Additional connections created as needed up to `maximum-pool-size`
- **Connection Reuse**: Existing idle connections are reused
- **Leak Detection**: Monitors for connection leaks based on threshold

## Supported Databases

- MySQL/MariaDB
- PostgreSQL
- H2 (for testing)
- Oracle
- SQL Server

## Best Practices

### Production Configuration
```yaml
db:
  connection:
    maximum-pool-size: 20
    minimum-idle: 10
    connection-timeout: 30000
    idle-timeout: 600000
    max-lifetime: 1800000
    leak-detection-threshold: 60000
```

### Development Configuration
```yaml
db:
  connection:
    maximum-pool-size: 5
    minimum-idle: 2
    connection-timeout: 10000
    leak-detection-threshold: 30000
```

## Monitoring

The library provides built-in logging for:
- Pool initialization success/failure
- Connection retry attempts
- Pool configuration details

Example log output:
```
INFO: Initializing HikariCPDataSource for connection pool: MyApp-Pool
INFO: Failed to initialize HikariCPDataSource for connection pool: MyApp-Pool
```

## Error Handling

The library implements robust error handling:
- **Connection Failures**: Automatic retry with exponential backoff
- **Pool Exhaustion**: Configurable timeout for connection acquisition
- **Database Downtime**: Graceful degradation with meaningful error messages

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Support

For issues and questions:
- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation for common solutions

## Changelog

### Version 1.0.0
- Initial release
- HikariCP integration
- Auto-configuration support
- Retry mechanism
- Comprehensive configuration options