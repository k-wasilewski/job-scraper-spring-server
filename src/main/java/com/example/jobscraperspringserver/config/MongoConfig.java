package com.example.jobscraperspringserver.config;

import java.util.Collection;
import java.util.Collections;
import org.springframework.context.annotation.Bean;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(name="jobscraperspringserver.mongo-config", havingValue="true", matchIfMissing=true)
@EnableReactiveMongoRepositories(basePackages = "com.example.jobscraperspringserver.repositories")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final String MONGODB_ENDPOINT = "mongodb://sa:password@mongodb:27017/scraping_db?authSource=admin";

    @Override
    protected String getDatabaseName() {
        return "scraping_db";
    }

    @Bean
    public MongoClient reactiveMongoClient() {
        ConnectionString connectionString = new ConnectionString(MONGODB_ENDPOINT);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.example.jobscraperspringserver.repositories");
    }
}
