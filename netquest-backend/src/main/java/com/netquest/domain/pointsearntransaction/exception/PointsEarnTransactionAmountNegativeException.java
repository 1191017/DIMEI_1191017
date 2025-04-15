package com.netquest.domain.pointsearntransaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PointsEarnTransactionAmountNegativeException extends RuntimeException {
    public PointsEarnTransactionAmountNegativeException(String message) {
        super(message);
    }
}
