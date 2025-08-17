package com.database.DatabaseConfig.properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.java.Log;

import java.sql.SQLException;

@Log
public class DataSourceFactory {
    public static HikariDataSource createDataSource(DatabaseConfigurationProperties databaseConfigurationProperties) {
        log.info("Initializing HikariCPDataSource for connection pool: "+databaseConfigurationProperties.getPoolName());
        HikariConfig hikariConfig = buildHikariConfig(databaseConfigurationProperties);
        int maxRetries = databaseConfigurationProperties.getMaxRetries();
        long retryDelay = databaseConfigurationProperties.getRetryDelay();
        try {
            return new HikariDataSource(hikariConfig);
        }catch (Exception e) {
            log.info("Failed to initialize HikariCPDataSource for connection pool: "+databaseConfigurationProperties.getPoolName());
            try {
                return getConnectionWithRetry(maxRetries, retryDelay, hikariConfig);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    private static HikariDataSource getConnectionWithRetry(int maxRetries, long retryDelay, HikariConfig hikariConfig) throws SQLException {

        for (int i = 0; i < maxRetries; i++) {
            try {
                return new HikariDataSource(hikariConfig);
            }catch (Exception e) {
                if (i < maxRetries) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        throw  new SQLException("retry process interrupted", ex);
                    }
                }
            }
        }
        throw new SQLException("Could not connect to database after " + maxRetries + " retries");
    }

    private static HikariConfig buildHikariConfig(DatabaseConfigurationProperties databaseConfigurationProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseConfigurationProperties.getUrl());
        hikariConfig.setUsername(databaseConfigurationProperties.getUsername());
        hikariConfig.setPassword(databaseConfigurationProperties.getPassword());
        hikariConfig.setDriverClassName(databaseConfigurationProperties.getDriverClassName());
        hikariConfig.setAutoCommit(databaseConfigurationProperties.isAutoCommit());
        hikariConfig.setMinimumIdle(databaseConfigurationProperties.getMinimumIdle());
        hikariConfig.setMaximumPoolSize(databaseConfigurationProperties.getMaximumPoolSize());
        hikariConfig.setPoolName(databaseConfigurationProperties.getPoolName());
        hikariConfig.setConnectionTimeout(databaseConfigurationProperties.getConnectionTimeout());
        hikariConfig.setIdleTimeout(databaseConfigurationProperties.getIdleTimeout());
        hikariConfig.setKeepaliveTime(databaseConfigurationProperties.getKeepAliveTime());
        hikariConfig.setMaxLifetime(databaseConfigurationProperties.getMaxLifetime());
        hikariConfig.setInitializationFailTimeout(databaseConfigurationProperties.getInitializationFailTimeout());
        hikariConfig.setIsolateInternalQueries(databaseConfigurationProperties.isIsolateInternalQueries());
        hikariConfig.setAllowPoolSuspension(databaseConfigurationProperties.isAllowPoolSuspension());
        hikariConfig.setValidationTimeout(databaseConfigurationProperties.getValidationTimeout());
        hikariConfig.setLeakDetectionThreshold(databaseConfigurationProperties.getLeakDetectionThreshold());
        hikariConfig.setReadOnly(databaseConfigurationProperties.isReadOnly());
        return hikariConfig;
    }
}
