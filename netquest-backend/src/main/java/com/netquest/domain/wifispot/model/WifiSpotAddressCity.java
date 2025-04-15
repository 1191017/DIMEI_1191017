package com.netquest.domain.wifispot.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class WifiSpotAddressCity {
    @NotNull
    private final String value;

    public WifiSpotAddressCity(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Address City cannot be null or empty");
        }
        this.value = value;
    }
}
