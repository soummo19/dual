package com.example.zkconsumer.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.example.zkconsumer")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final MongoEnvironmentConfig mongoEnvironmentConfig;

    @Override
    protected String getDatabaseName() {
        return mongoEnvironmentConfig.getMongoDbName();
    }

    @Override
    public boolean autoIndexCreation() {
        return true;
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.example.zkconsumer");
    }
    
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(
            MongoClientSettings.builder()
                .applyConnectionString(
                    new ConnectionString(mongoEnvironmentConfig.getConnectionString())
            )
            .build()
        );
    }

}
