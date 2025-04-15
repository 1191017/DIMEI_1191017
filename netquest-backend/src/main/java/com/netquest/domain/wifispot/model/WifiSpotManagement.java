package com.netquest.domain.wifispot.model;

import com.netquest.domain.shared.WifiSpotManagementType;
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
public class WifiSpotManagement {
    @NotNull
    private final WifiSpotManagementType value;

    public WifiSpotManagement(WifiSpotManagementType value) {
        if (value == null) {
            throw new IllegalArgumentException("Management Type cannot be null");
        }
        this.value = value;
    }
}
