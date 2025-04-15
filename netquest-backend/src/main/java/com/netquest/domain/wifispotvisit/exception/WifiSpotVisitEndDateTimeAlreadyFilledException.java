package com.netquest.domain.wifispotvisit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class WifiSpotVisitEndDateTimeAlreadyFilledException extends RuntimeException{
    public WifiSpotVisitEndDateTimeAlreadyFilledException(String message){ super(message); }
}
