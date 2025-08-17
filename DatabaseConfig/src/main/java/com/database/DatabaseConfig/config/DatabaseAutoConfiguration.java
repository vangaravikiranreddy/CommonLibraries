package com.database.DatabaseConfig.config;

import com.database.DatabaseConfig.properties.DataSourceFactory;
import com.database.DatabaseConfig.properties.DatabaseConfigurationProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DatabaseConfigurationProperties.class)
public class DatabaseAutoConfiguration {
    @Bean
    public HikariDataSource hikariDataSource(DatabaseConfigurationProperties databaseConfigurationProperties) {
        return DataSourceFactory.createDataSource(databaseConfigurationProperties);
    }
}
