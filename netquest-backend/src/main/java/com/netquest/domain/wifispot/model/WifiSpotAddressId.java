package com.netquest.domain.wifispot.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class WifiSpotAddressId {
    private final UUID value;

    public WifiSpotAddressId(UUID value) {
        this.value = value;
    }

    public WifiSpotAddressId() {
        this.value = UUID.randomUUID();
    }
}
