package com.netquest.domain.wifispotvisit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FutureWifiSpotVisitEndDateTimeException extends RuntimeException {
    public FutureWifiSpotVisitEndDateTimeException(String message) {super(message);}
}
