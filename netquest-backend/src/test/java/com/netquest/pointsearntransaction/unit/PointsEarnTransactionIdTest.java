package com.netquest.pointsearntransaction.unit;

import com.netquest.domain.pointsearntransaction.model.PointsEarnTransactionId;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PointsEarnTransactionIdTest {
    @Test
    public void PointsEarnTransactionIdRandomGenerateTest() {
        int numInstances = 100;
        Set<PointsEarnTransactionId> ids = new HashSet<>();

        for (int i = 0; i < numInstances; i++) {
            PointsEarnTransactionId pointsEarnTransactionId = new PointsEarnTransactionId();
            ids.add(pointsEarnTransactionId);
        }

        // Check that all IDs are unique by comparing the set size to the number of instances
        assertThat(ids.size()).isEqualTo(numInstances);
    }
}
