package com.netquest.wifispotvisit.unit;

import com.netquest.domain.wifispotvisit.model.WifiSpotVisitId;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WifSpotVisitIdTest {

    @Test
    public void WifiSpotVisitIdRandomGenerateTest() {
        int numInstances = 100;
        Set<WifiSpotVisitId> ids = new HashSet<>();

        for (int i = 0; i < numInstances; i++) {
            WifiSpotVisitId wifiSpotVisitId = new WifiSpotVisitId();
            ids.add(wifiSpotVisitId);
        }

        // Check that all IDs are unique by comparing the set size to the number of instances
        assertThat(ids.size()).isEqualTo(numInstances);
    }
}
