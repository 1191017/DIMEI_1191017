package com.netquest.domain.pointsearntransaction.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class PointsEarnTransactionDateTime {
    private final LocalDateTime value;
    public PointsEarnTransactionDateTime(){
        this.value = LocalDateTime.now();
    }

}
