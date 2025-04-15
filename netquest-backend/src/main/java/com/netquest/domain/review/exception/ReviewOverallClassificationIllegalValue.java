package com.netquest.domain.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReviewOverallClassificationIllegalValue extends RuntimeException {
    public ReviewOverallClassificationIllegalValue(String message) {
        super(message);
    }
}
