package com.netquest.domain.wifispotvisit.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class WifiSpotVisitId {

    private final UUID value;

    public WifiSpotVisitId(UUID value) {
        this.value = value;
    }

    public WifiSpotVisitId() {
        this.value = UUID.randomUUID();
    }

}
