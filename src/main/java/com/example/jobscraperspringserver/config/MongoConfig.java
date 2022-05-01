package com.example.jobscraperspringserver.config;

import java.util.Collection;
import java.util.Collections;

import com.example.jobscraperspringserver.utils.HostUtils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.jobscraperspringserver.repositories")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final String MONGODB_ENDPOINT = HostUtils.pingHost("localhost", 27017, 3000) ? "mongodb://sa:password@localhost:27017/test?authSource=admin" : "mongodb://sa:password@mongodb:27017/test?authSource=admin";

    @Override
    protected String getDatabaseName() {
        return "scraping_db";
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(MONGODB_ENDPOINT);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.baeldung");
    }
}
