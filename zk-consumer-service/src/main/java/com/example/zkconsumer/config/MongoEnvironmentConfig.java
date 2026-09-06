package com.example.zkconsumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "mongodb")
public class MongoEnvironmentConfig {
    private String mongoDbName;
    private String connectionString;
}
