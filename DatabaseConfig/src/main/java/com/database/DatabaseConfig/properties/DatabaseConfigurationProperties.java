package com.database.DatabaseConfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "db.connection")
public class DatabaseConfigurationProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int maximumPoolSize = 10;
    private int minimumIdle = 5;
    private int idleTimeout = 300000;
    private int connectionTimeout = 20000;
    private int maxLifetime = 1800000;
    private long keepAliveTime = 120000;
    private boolean autoCommit = true;
    private String poolName = "default";
    private long initializationFailTimeout = 1;
    private boolean isolateInternalQueries = false;
    private boolean allowPoolSuspension = false;
    private  long validationTimeout = 5000;
    private long leakDetectionThreshold = 2000;
    private int maxRetries = 3;
    private long retryDelay = 3000;
    private boolean readOnly = false;
}
