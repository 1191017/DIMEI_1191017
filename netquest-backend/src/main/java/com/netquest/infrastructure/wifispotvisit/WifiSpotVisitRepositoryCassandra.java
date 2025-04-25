package com.netquest.infrastructure.wifispotvisit;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitCreateDto;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class WifiSpotVisitRepositoryCassandra {
    private final CqlSession session;

    public WifiSpotVisitRepositoryCassandra(CqlSession session) {
        this.session = session;
    }

    public void createWifiSpotVisit(UUID userId, WifiSpotVisitCreateDto dto) {
        String query = """
            INSERT INTO wifi_spot_visit (
                visit_id,
                user_id,
                wifi_spot_id,
                wifi_spot_name,
                wifi_spot_location,
                visit_start,
                visit_end
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        session.execute(session
                .prepare(query)
                .bind(
                        dto.getWifiSpotVisitId(),
                        userId,
                        dto.getWifiSpotId(),
                        getWifiSpotName(dto.getWifiSpotId()),
                        getWifiSpotLocation(dto.getWifiSpotId()),
                        Instant.from(dto.getStartDateTime().atZone(java.time.ZoneOffset.UTC)),
                        dto.getEndDateTime() != null
                                ? Instant.from(dto.getEndDateTime().atZone(java.time.ZoneOffset.UTC))
                                : null
                )
        );
    }

    public String getWifiSpotName(UUID wifiSpotId) {
        String query = "SELECT wifi_spot_name FROM wifi_spot WHERE wifi_spot_id = ?";

        return session.execute(session.prepare(query).bind(wifiSpotId))
                .one()
                .getString("wifi_spot_name");
    }

    public String getWifiSpotLocation(UUID wifiSpotId) {
        String query = "SELECT wifi_spot_address_line_1, wifi_spot_address_city FROM wifi_spot WHERE wifi_spot_id = ?";

        var row = session.execute(session.prepare(query).bind(wifiSpotId)).one();
        if (row == null) return "Unknown";

        String line1 = row.getString("wifi_spot_address_line_1");
        String city = row.getString("wifi_spot_address_city");

        return line1 + ", " + city;
    }
}
