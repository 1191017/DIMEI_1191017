package com.netquest.pointsearntransaction.unit;

import com.netquest.domain.pointsearntransaction.model.PointsEarnTransactionDateTime;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

public class PointsEarnTransactionDateTimeTest {

    @Test
    public void pointsEarnTransactionDateTimeTest() {
        LocalDateTime now = LocalDateTime.now();
        PointsEarnTransactionDateTime pointsEarnTransactionDateTime = new PointsEarnTransactionDateTime();
        LocalDateTime now2 = LocalDateTime.now();
        assertThat(pointsEarnTransactionDateTime.getValue()).isBetween(now, now2);
    }
}
