package com.netquest.domain.wifispotvisit.model;

import com.netquest.domain.wifispotvisit.exception.FutureWifiSpotVisitEndDateTimeException;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class WifiSpotVisitEndDateTime implements Comparable<WifiSpotVisitEndDateTime> {
    private final LocalDateTime value;

    public WifiSpotVisitEndDateTime(LocalDateTime value) {
        if(value != null && value.isAfter(LocalDateTime.now())){
            throw new FutureWifiSpotVisitEndDateTimeException("Wifi Spot Visit End DateTime cannot be in the future");
        }
        this.value = value;
    }


    @Override
    public int compareTo(WifiSpotVisitEndDateTime other) {
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
