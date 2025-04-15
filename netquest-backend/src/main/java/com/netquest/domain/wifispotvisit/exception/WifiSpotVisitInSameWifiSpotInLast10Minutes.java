package com.netquest.domain.wifispotvisit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class WifiSpotVisitInSameWifiSpotInLast10Minutes extends RuntimeException {
    public WifiSpotVisitInSameWifiSpotInLast10Minutes(String message) {
        super(message);
    }
}
