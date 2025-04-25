package com.netquest.infrastructure.wifispot;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class WifiSpotRepositoryCassandra {
    private final CqlSession session;

    public WifiSpotRepositoryCassandra(CqlSession session) {
        this.session = session;
    }

    public void updateWifiSpotName(UUID wifiSpotId, String newName) {
        String updateSpotQuery = "UPDATE wifi_spot SET wifi_spot_name = ? WHERE wifi_spot_id = ?";
        session.execute(session.prepare(updateSpotQuery).bind(newName, wifiSpotId));

        String selectVisits = "SELECT visit_id FROM wifi_spot_visit WHERE wifi_spot_id = ?";
        PreparedStatement selectStmt = session.prepare(selectVisits);
        ResultSet resultSet = session.execute(selectStmt.bind(wifiSpotId));

        String updateVisitQuery = "UPDATE wifi_spot_visit SET wifi_spot_name = ? WHERE visit_id = ?";
        PreparedStatement updateVisitStmt = session.prepare(updateVisitQuery);

        for (Row row : resultSet) {
            session.execute(updateVisitStmt.bind(
                    newName,
                    row.getUuid("visit_id")
            ));
        }
    }

    public void createWifiSpot(WifiSpotCreateDto dto) {
        WifiSpotAddressCreateDto addr = dto.address();

        String query = """
            INSERT INTO wifi_spot (
                wifi_spot_id,
                wifi_spot_name,
                wifi_spot_latitude,
                wifi_spot_longitude,
                wifi_spot_description,
                wifi_spot_air_conditioning,
                wifi_spot_child_friendly,
                wifi_spot_covered_area,
                wifi_spot_crowded,
                wifi_spot_disabled_access,
                wifi_spot_good_view,
                wifi_spot_noise_level,
                wifi_spot_signal_strength,
                wifi_spot_wifi_quality,
                wifi_spot_bandwidth_limitations,
                wifi_spot_location_type,
                wifi_spot_management,
                wifi_spot_available_power_outlets,
                wifi_spot_charging_stations,
                wifi_spot_pet_friendly,
                wifi_spot_food_options,
                wifi_spot_drink_options,
                wifi_spot_restrooms_available,
                wifi_spot_parking_availability,
                wifi_spot_user_id,
                wifi_spot_create_date_time,
                wifi_spot_address_line_1,
                wifi_spot_address_line_2,
                wifi_spot_address_city,
                wifi_spot_address_district,
                wifi_spot_address_country,
                wifi_spot_address_zip_code
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        session.execute(session
                .prepare(query)
                .bind(
                        dto.id(),
                        dto.name(),
                        dto.latitude(),
                        dto.longitude(),
                        dto.description(),
                        dto.airConditioning(),
                        dto.childFriendly(),
                        dto.coveredArea(),
                        dto.crowded(),
                        dto.disableAccess(),
                        dto.goodView(),
                        1,
                        1,
                        1,
                        true, // true if LIMITED
                        1,
                        1,
                        dto.availablePowerOutlets(),
                        dto.chargingStations(),
                        dto.petFriendly(),
                        dto.foodOptions(),
                        dto.drinkOptions(),
                        dto.restroomsAvailable(),
                        dto.parkingAvailability(),
                        null, // wifi_spot_user_id not passed in DTO
                        Instant.now(),
                        addr.addressLine1(),
                        addr.addressLine2(),
                        addr.city(),
                        addr.district(),
                        addr.country(),
                        addr.zipCode()
                )
        );
    }
}
