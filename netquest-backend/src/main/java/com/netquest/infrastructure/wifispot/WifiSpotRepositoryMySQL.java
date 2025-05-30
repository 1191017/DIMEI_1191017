package com.netquest.infrastructure.wifispot;

import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import org.hibernate.type.SqlTypes;
import org.springframework.stereotype.Repository;
import java.sql.*;

import java.util.UUID;

@Repository
public class WifiSpotRepositoryMySQL {
    private final String url = "jdbc:mysql://192.168.1.9:3306/netQuest";
    private final String username = "netQuest";
    private final String password = "netQuestLocal";

    public WifiSpotRepositoryMySQL() {}

    // 5.3.2 - Update a Wi-Fi spot name
    public int updateWifiSpotName(UUID wifiSpotId, String newName) {
        String sql = "UPDATE wifi_spot SET wifi_spot_name = ? WHERE wifi_spot_id = UUID_TO_BIN(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setString(2, wifiSpotId.toString());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Wi-Fi spot name", e);
        }
    }

    public void createWifiSpot(UUID userId, WifiSpotCreateDto dto) {
        String insertAddressSql = """
            INSERT INTO wifi_spot_address (
                wifi_spot_address_id,
                wifi_spot_address_country,
                wifi_spot_address_zip_code,
                wifi_spot_address_line_1,
                wifi_spot_address_line_2,
                wifi_spot_address_city,
                wifi_spot_address_district
            ) VALUES (
                UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?
            )
        """;

        String insertSpotSql = """
            INSERT INTO wifi_spot (
                wifi_spot_id,
                wifi_spot_latitude,
                wifi_spot_longitude,
                wifi_spot_create_date_time,
                wifi_spot_description,
                wifi_spot_air_conditioning,
                wifi_spot_child_friendly,
                wifi_spot_covered_area,
                wifi_spot_crowded,
                wifi_spot_disabled_access,
                wifi_spot_good_view,
                wifi_spot_noise_level,
                wifi_spot_outdoor_seating,
                wifi_spot_pet_friendly,
                wifi_spot_available_power_outlets,
                wifi_spot_charging_stations,
                wifi_spot_drink_options,
                wifi_spot_food_options,
                wifi_spot_parking_availability,
                wifi_spot_restrooms_available,
                wifi_spot_location_type,
                wifi_spot_management,
                wifi_spot_name,
                wifi_spot_bandwith_limitations,
                end_time,
                start_time,
                wifi_spot_signal_strength,
                wifi_spot_wifi_quality,
                wifi_spot_heated_in_winter,
                wifi_spot_open_during_heat,
                wifi_spot_open_during_rain,
                wifi_spot_outdoor_fans,
                wifi_spot_shaded_areas,
                wifi_spot_address_id,
                wifi_spot_user_id
            ) VALUES (
                UUID_TO_BIN(?), ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, UUID_TO_BIN(?), UUID_TO_BIN(?)
            )
        """;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.setAutoCommit(false);

            try (PreparedStatement addressStmt = connection.prepareStatement(insertAddressSql);
                 PreparedStatement spotStmt = connection.prepareStatement(insertSpotSql)) {

                WifiSpotAddressCreateDto addr = dto.address();

                // Inserir morada
                addressStmt.setString(1, addr.id());
                addressStmt.setString(2, addr.country());
                addressStmt.setString(3, addr.zipCode());
                addressStmt.setString(4, addr.addressLine1());
                addressStmt.setString(5, addr.addressLine2());
                addressStmt.setString(6, addr.city());
                addressStmt.setString(7, addr.district());
                addressStmt.executeUpdate();

                // Inserir WiFi Spot
                spotStmt.setString(1, dto.id().toString());
                spotStmt.setDouble(2, dto.latitude());
                spotStmt.setDouble(3, dto.longitude());
                spotStmt.setString(4, dto.description());
                spotStmt.setBoolean(5, dto.airConditioning());
                spotStmt.setBoolean(6, dto.childFriendly());
                spotStmt.setBoolean(7, dto.coveredArea());
                spotStmt.setBoolean(8, dto.crowded());
                spotStmt.setBoolean(9, dto.disableAccess());
                spotStmt.setBoolean(10, dto.goodView());
                spotStmt.setInt(11, 1);
                spotStmt.setBoolean(12, dto.outdoorSeating());
                spotStmt.setBoolean(13, dto.petFriendly());
                spotStmt.setBoolean(14, dto.availablePowerOutlets());
                spotStmt.setBoolean(15, dto.chargingStations());
                spotStmt.setBoolean(16, dto.drinkOptions());
                spotStmt.setBoolean(17, dto.foodOptions());
                spotStmt.setBoolean(18, dto.parkingAvailability());
                spotStmt.setBoolean(19, dto.restroomsAvailable());
                spotStmt.setInt(20, 1);
                spotStmt.setInt(21, 1);
                spotStmt.setString(22, dto.name());
                spotStmt.setInt(23, 1);
                spotStmt.setTime(24, dto.peakUsageEnd() != null ? Time.valueOf(dto.peakUsageEnd()) : null);
                spotStmt.setTime(25, dto.peakUsageStart() != null ? Time.valueOf(dto.peakUsageStart()) : null);
                spotStmt.setInt(26, 1);
                spotStmt.setInt(27, 1);
                spotStmt.setBoolean(28, dto.heatedInWinter());
                spotStmt.setBoolean(29, dto.openDuringHeat());
                spotStmt.setBoolean(30, dto.openDuringRain());
                spotStmt.setBoolean(31, dto.outdoorFans());
                spotStmt.setBoolean(32, dto.shadedAreas());
                spotStmt.setString(33, addr.id());
                spotStmt.setString(34, userId.toString());

                spotStmt.executeUpdate();
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error creating Wi-Fi Spot and Address", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }
}
