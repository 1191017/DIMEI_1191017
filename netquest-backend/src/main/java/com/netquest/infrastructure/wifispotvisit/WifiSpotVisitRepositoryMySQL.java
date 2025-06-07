package com.netquest.infrastructure.wifispotvisit;

import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitCreateDto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class WifiSpotVisitRepositoryMySQL {
    private final String url = "jdbc:mysql://192.168.1.252:3306/netQuest";
    private final String username = "netQuest";
    private final String password = "netQuestLocal";

    public WifiSpotVisitRepositoryMySQL() {}

    public void createWifiSpotVisit(UUID userId, WifiSpotVisitCreateDto dto) {
        String sql = """
            INSERT INTO wifi_spot_visit (
                wifi_spot_visit_id,
                wifi_spot_visit_user_id,
                wifi_spot_visit_wifi_spot_id,
                wifi_spot_visit_start_datetime,
                wifi_spot_visit_end_datetime
            ) VALUES (
                UUID_TO_BIN(?), UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?
            )
        """;

        UUID visitId = dto.getWifiSpotVisitId() != null ? dto.getWifiSpotVisitId() : UUID.randomUUID();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, visitId.toString());
            stmt.setString(2, userId.toString());
            stmt.setString(3, dto.getWifiSpotId().toString());
            stmt.setTimestamp(4, Timestamp.valueOf(dto.getStartDateTime()));
            stmt.setTimestamp(5, dto.getEndDateTime() != null ? Timestamp.valueOf(dto.getEndDateTime()) : null);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error creating Wi-Fi spot visit", e);
        }
    }
}
