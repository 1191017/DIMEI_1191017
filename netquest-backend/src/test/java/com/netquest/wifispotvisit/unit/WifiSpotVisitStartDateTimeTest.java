package com.netquest.wifispotvisit.unit;

import com.netquest.domain.wifispotvisit.exception.FutureWifiSpotVisitStartDateTimeException;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitStartDateTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class WifiSpotVisitStartDateTimeTest {

    @Test
    public void WifiSpotVisitStartDateTimeNullTest() {
        NullPointerException exception = catchThrowableOfType(
                () ->
                        new WifiSpotVisitStartDateTime(null),
                NullPointerException.class
        );

        assertThat(exception.getMessage()).isNotEmpty();
    }

    @Test
    public void WifiSpotVisitStartDateTimeFutureTest() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        FutureWifiSpotVisitStartDateTimeException exception = catchThrowableOfType(
                () ->
                        new WifiSpotVisitStartDateTime(localDateTime),
                FutureWifiSpotVisitStartDateTimeException.class
        );

        assertThat(exception.getMessage()).isNotEmpty();
    }

    @Test void WifiSpotVisitStartDateTimeEqualsTest(){
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime1 = new WifiSpotVisitStartDateTime(localDateTime);
        WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime2 = new WifiSpotVisitStartDateTime(localDateTime);
        assertThat(wifiSpotVisitStartDateTime1).isEqualTo(wifiSpotVisitStartDateTime2);

    }

    @Test void WifiSpotVisitStartDateTimeBiggerTest(){
        LocalDateTime localDateTime1 = LocalDateTime.now().minusDays(1);
        LocalDateTime localDateTime2 = LocalDateTime.now().minusDays(2);
        WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime1 = new WifiSpotVisitStartDateTime(localDateTime1);
        WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime2 = new WifiSpotVisitStartDateTime(localDateTime2);
        assertThat(wifiSpotVisitStartDateTime1.compareTo(wifiSpotVisitStartDateTime2) > 0).isTrue();
    }

    @Test void WifiSpotVisitStartDateTimeSmallerTest(){
        LocalDateTime localDateTime1 = LocalDateTime.now().minusDays(2);
        LocalDateTime localDateTime2 = LocalDateTime.now().minusDays(1);
        WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime1 = new WifiSpotVisitStartDateTime(localDateTime1);
        WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime2 = new WifiSpotVisitStartDateTime(localDateTime2);
        assertThat(wifiSpotVisitStartDateTime1.compareTo(wifiSpotVisitStartDateTime2) < 0).isTrue();
    }
}
