package com.netquest.domain.wifispot.service.impl;


import com.netquest.domain.wifispot.service.WifiSpotService;
import com.netquest.infrastructure.wifispot.WifiSpotRepositoryCassandra;
import com.netquest.infrastructure.wifispot.WifiSpotRepositoryMongoDB;
import com.netquest.infrastructure.wifispot.WifiSpotRepositoryMySQL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WifiSpotServiceImpl implements WifiSpotService {
    private final WifiSpotRepositoryMySQL wifiSpotRepositoryMySQL;
    private final WifiSpotRepositoryMongoDB wifiSpotRepositoryMongoDB;
    private final WifiSpotRepositoryCassandra wifiSpotRepositoryCassandra;

    // 5.3.2 - Update Wi-Fi spot description
    private void updateWifiSpotName(UUID wifiSpotId, String newName, boolean mysql, boolean mongodb, boolean cassandra) {
        if (mysql) {
            wifiSpotRepositoryMySQL.updateWifiSpotName(wifiSpotId, newName);
        } else if (mongodb) {
            wifiSpotRepositoryMongoDB.updateWifiSpotName(wifiSpotId, newName);
        }
        else if(cassandra){
            wifiSpotRepositoryCassandra.updateWifiSpotName(wifiSpotId, newName);
        }
    }

    @Override
    public void updateAllWifiSpotNames(List<Pair<UUID, String>> updates, boolean mysql, boolean mongodb, boolean cassandra) {
        for (Pair<UUID, String> entry : updates) {
            updateWifiSpotName(entry.getLeft(), entry.getRight(), mysql, mongodb, cassandra);
        }
    }
}

