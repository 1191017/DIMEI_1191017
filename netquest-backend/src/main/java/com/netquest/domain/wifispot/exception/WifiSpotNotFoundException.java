package com.netquest.domain.wifispot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WifiSpotNotFoundException extends RuntimeException {

    public WifiSpotNotFoundException(String message) {
        super(message);
    }
}
