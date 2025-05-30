package com.netquest;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfig {

    @Bean
    public MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(new com.mongodb.ConnectionString("mongodb://netQuest:netQuestLocal@192.168.1.9:27017"))
                .build();
        return MongoClients.create(settings);
    }
}
