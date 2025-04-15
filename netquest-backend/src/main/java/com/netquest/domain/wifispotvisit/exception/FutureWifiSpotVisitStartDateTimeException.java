package com.netquest.domain.wifispotvisit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FutureWifiSpotVisitStartDateTimeException extends RuntimeException {
    public FutureWifiSpotVisitStartDateTimeException(String message) {super(message);}
}
