package com.netquest.infrastructure.wifispot;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import org.bson.Document;

import com.mongodb.client.model.Updates;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
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

    public void createWifiSpot(WifiSpotCreateDto dto) {
        // Map 'features'
        Document features = new Document()
                .append("air_conditioning", dto.airConditioning())
                .append("child_friendly", dto.childFriendly())
                .append("covered_area", dto.coveredArea())
                .append("crowded", dto.crowded())
                .append("disabled_access", dto.disableAccess())
                .append("good_view", dto.goodView())
                .append("noise_level", 1)
                .append("signal_strength", 1)
                .append("wifi_quality", 1)
                .append("bandwidth_limitations", true) // true if LIMITED
                .append("location_type", 1)
                .append("management", 1)
                .append("available_power_outlets", dto.availablePowerOutlets())
                .append("charging_stations", dto.chargingStations())
                .append("pet_friendly", dto.petFriendly())
                .append("food_options", dto.foodOptions())
                .append("drink_options", dto.drinkOptions())
                .append("restrooms_available", dto.restroomsAvailable())
                .append("parking_availability", dto.parkingAvailability())
                .append("heated_in_winter", dto.heatedInWinter())
                .append("open_during_heat", dto.openDuringHeat())
                .append("open_during_rain", dto.openDuringRain())
                .append("outdoor_fans", dto.outdoorFans())
                .append("shaded_areas", dto.shadedAreas());

        // Map 'address'
        WifiSpotAddressCreateDto addressDto = dto.address();
        Document address = new Document()
                .append("city", addressDto.city())
                .append("country", addressDto.country())
                .append("district", addressDto.district())
                .append("line1", addressDto.addressLine1())
                .append("line2", addressDto.addressLine2())
                .append("zip_code", addressDto.zipCode());

        // Create the main document
        Document document = new Document()
                .append("_id", dto.id())
                .append("user_id", null) // Assumindo que ainda não passas o user aqui. Podes adaptar.
                .append("name", dto.name())
                .append("latitude", dto.latitude())
                .append("longitude", dto.longitude())
                .append("description", dto.description())
                .append("features", features)
                .append("address", address)
                .append("create_date_time", Date.from(Instant.now()));

        collection.insertOne(document);
    }
}
