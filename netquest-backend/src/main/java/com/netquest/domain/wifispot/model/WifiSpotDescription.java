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
public class WifiSpotDescription {
    @NotNull
    private final String value;

    public WifiSpotDescription(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.value = value;
    }
}
