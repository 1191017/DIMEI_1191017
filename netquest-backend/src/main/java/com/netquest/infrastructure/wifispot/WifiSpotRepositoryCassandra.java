package com.netquest.infrastructure.wifispot;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class WifiSpotRepositoryCassandra {
    private final CqlSession session;

    public WifiSpotRepositoryCassandra(CqlSession session) {
        this.session = session;
    }

    // 5.3.2 - Update Wi-Fi spot name
    public void updateWifiSpotName(UUID wifiSpotId, String newName) {
        // 1. Update wifi_spot
        String updateSpotQuery = "UPDATE wifi_spot SET wifi_spot_name = ? WHERE wifi_spot_id = ?";
        session.execute(session.prepare(updateSpotQuery).bind(newName, wifiSpotId));

        // 2. Get all visits that have this wifi_spot_id
        String selectVisits = "SELECT visit_id FROM wifi_spot_visit WHERE wifi_spot_id = ?";
        PreparedStatement selectStmt = session.prepare(selectVisits);
        ResultSet resultSet = session.execute(selectStmt.bind(wifiSpotId));

        // 3. Update each matching visit row
        String updateVisitQuery = "UPDATE wifi_spot_visit SET wifi_spot_name = ? WHERE visit_id = ?";
        PreparedStatement updateVisitStmt = session.prepare(updateVisitQuery);

        for (Row row : resultSet) {
            session.execute(updateVisitStmt.bind(
                    newName,
                    row.getUuid("visit_id")
            ));
        }
    }
}
