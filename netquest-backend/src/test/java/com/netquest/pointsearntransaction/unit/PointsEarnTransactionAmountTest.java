package com.netquest.pointsearntransaction.unit;

import com.netquest.domain.pointsearntransaction.exception.PointsEarnTransactionAmountNegativeException;
import com.netquest.domain.pointsearntransaction.model.PointsEarnTransactionAmount;
import com.netquest.domain.wifispotvisit.exception.FutureWifiSpotVisitStartDateTimeException;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitStartDateTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class PointsEarnTransactionAmountTest {
    @Test
    public void pointsEarnTransactionAmountGoodTest() {
        long amount = Long.MAX_VALUE;
        PointsEarnTransactionAmount pointsEarnTransactionAmount = new PointsEarnTransactionAmount(amount);
        assertThat(pointsEarnTransactionAmount.getValue() == amount).isTrue();
    }

    @Test
    public void pointsEarnTransactionAmountNegativeTest() {
        long amount = Long.MIN_VALUE;
        PointsEarnTransactionAmountNegativeException exception = catchThrowableOfType(
                () ->
                        new PointsEarnTransactionAmount(amount),
                PointsEarnTransactionAmountNegativeException.class
        );

        assertThat(exception.getMessage()).isNotEmpty();

    }

}
