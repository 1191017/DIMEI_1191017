package com.netquest.infrastructure.wifispot;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import com.mongodb.client.model.Updates;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class WifiSpotRepositoryMongoDB {
    private final MongoCollection<Document> collection;
    private final MongoCollection<Document> wifiSpotVisitCollection;

    public WifiSpotRepositoryMongoDB(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("netQuest");
        this.collection = database.getCollection("wifi_spot");
        this.wifiSpotVisitCollection = database.getCollection("wifi_spot_visit");
    }

    // 5.3.2 - Update a Wi-Fi spot name
    public void updateWifiSpotName(UUID wifiSpotId, String newName) {

        collection.updateOne(
                Filters.eq("_id", wifiSpotId),
                Updates.set("name", newName)
        );

        wifiSpotVisitCollection.updateMany(
                Filters.eq("wifi_spot._id", wifiSpotId),
                Updates.set("wifi_spot.name", newName)
        );
    }
}
