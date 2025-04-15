package com.netquest.domain.pointsearntransaction.model;

import com.netquest.domain.pointsearntransaction.exception.PointsEarnTransactionAmountNegativeException;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class PointsEarnTransactionAmount {
    private final long value;

    public PointsEarnTransactionAmount(long value) {
        if (value < 0) {
            throw new PointsEarnTransactionAmountNegativeException("Points Earned Amount cannot be negative");
        }
        this.value = value;
    }
}
