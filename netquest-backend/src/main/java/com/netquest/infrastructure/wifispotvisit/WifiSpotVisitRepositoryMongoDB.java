package com.netquest.infrastructure.wifispotvisit;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitCreateDto;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Repository
public class WifiSpotVisitRepositoryMongoDB {
    private final MongoCollection<Document> wifiSpotVisitCollection;
    private final MongoCollection<Document> wifiSpotCollection;

    public WifiSpotVisitRepositoryMongoDB(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("netQuest");
        this.wifiSpotVisitCollection = database.getCollection("wifi_spot_visit");
        this.wifiSpotCollection = database.getCollection("wifi_spot");
    }

    public void createWifiSpotVisit(UUID userId, WifiSpotVisitCreateDto dto) {
        // Obter o Wi-Fi spot referenciado (name + location) — mínimo necessário para embutir
        Document wifiSpot = wifiSpotCollection.find(new Document("_id", dto.getWifiSpotId())).first();

        if (wifiSpot == null) {
            throw new IllegalArgumentException("Wi-Fi spot not found with ID: " + dto.getWifiSpotId());
        }

        // Extrair nome e local (podes adaptar este campo 'location' conforme o design real)
        String spotName = wifiSpot.getString("name");
        Document address = (Document) wifiSpot.get("address");
        String location = address != null
                ? address.getString("line1") + ", " + address.getString("city")
                : "Unknown Location";

        Document embeddedWifiSpot = new Document()
                .append("_id", dto.getWifiSpotId())
                .append("name", spotName)
                .append("location", location);

        Document visit = new Document()
                .append("_id", dto.getWifiSpotVisitId())
                .append("user_id", userId)
                .append("wifi_spot", embeddedWifiSpot)
                .append("start_time", Date.from(dto.getStartDateTime().atZone(ZoneOffset.UTC).toInstant()))
                .append("end_time", dto.getEndDateTime() != null
                        ? Date.from(dto.getEndDateTime().atZone(ZoneOffset.UTC).toInstant())
                        : null);

        wifiSpotVisitCollection.insertOne(visit);
    }
}
