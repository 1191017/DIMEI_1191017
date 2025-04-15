package com.netquest.domain.pointsearntransaction.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class PointsEarnTransactionId {
    private final UUID value;

    public PointsEarnTransactionId(UUID value) {
        this.value = value;
    }

    public PointsEarnTransactionId() {
        this.value = UUID.randomUUID();
    }
}
