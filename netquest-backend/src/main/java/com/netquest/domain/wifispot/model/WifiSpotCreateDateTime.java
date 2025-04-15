package com.netquest.domain.wifispot.model;

import com.netquest.domain.wifispotvisit.exception.FutureWifiSpotVisitEndDateTimeException;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class WifiSpotCreateDateTime implements Comparable<WifiSpotCreateDateTime> {
    private final LocalDateTime value;

    public WifiSpotCreateDateTime(LocalDateTime value) {
        this.value = value;
    }

    public WifiSpotCreateDateTime() {
        this.value = LocalDateTime.now();
    }


    @Override
    public int compareTo(WifiSpotCreateDateTime other) {
        if ((other == null || other.value == null) && this.value == null) {
            return 0;
        }
        if (this.value == null) {
            return -1;  // Null is considered "less than" any non-null value
        }
        if (other == null || other.value == null) {
            return 1;  // Any non-null value is considered "greater than" null
        }
        return this.value.compareTo(other.value);
    }
}
