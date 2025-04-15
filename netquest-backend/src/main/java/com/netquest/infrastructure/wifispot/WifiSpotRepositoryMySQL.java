package com.netquest.infrastructure.wifispot;

import org.springframework.stereotype.Repository;
import java.sql.*;

import java.util.UUID;

@Repository
public class WifiSpotRepositoryMySQL {
    private final String url = "jdbc:mysql://localhost:3306/netQuest";
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
}
