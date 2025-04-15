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
public class WifiSpotId {
    private final UUID value;

    public WifiSpotId(UUID value) {
        this.value = value;
    }

    public WifiSpotId() {
        this.value = UUID.randomUUID();
    }
}
