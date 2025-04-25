package com.netquest.domain.wifispot.service;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.UUID;

public interface WifiSpotService {
    void updateAllWifiSpotNames(List<Pair<UUID, String>> updates, boolean mysql, boolean mongodb, boolean cassandra);

    void importFromCsv();

    void importFromCsvMongodb();
    void importFromCsvCassandra();
}
