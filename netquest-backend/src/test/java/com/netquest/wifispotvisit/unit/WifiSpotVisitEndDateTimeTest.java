package com.netquest.wifispotvisit.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.netquest.domain.wifispotvisit.exception.FutureWifiSpotVisitEndDateTimeException;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitEndDateTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class WifiSpotVisitEndDateTimeTest {

    @Test
    public void WifiSpotVisitEndDateTimeNullTest() {
        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTimeNull = new WifiSpotVisitEndDateTime(null);
        assertThat(wifiSpotVisitEndDateTimeNull.getValue()).isNull();
    }

    @Test
    public void WifiSpotVisitEndDateTimeEmptyTest() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        FutureWifiSpotVisitEndDateTimeException exception = catchThrowableOfType(
                () ->
                        new WifiSpotVisitEndDateTime(localDateTime),
                FutureWifiSpotVisitEndDateTimeException.class
        );

        assertThat(exception.getMessage()).isNotEmpty();
    }

    @Test void WifiSpotVisitEndDateTimeEqualsTest(){
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime1 = new WifiSpotVisitEndDateTime(localDateTime);
        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime2 = new WifiSpotVisitEndDateTime(localDateTime);
        assertThat(wifiSpotVisitEndDateTime1).isEqualTo(wifiSpotVisitEndDateTime2);

    }

    @Test void WifiSpotVisitEndDateTimeBiggerTest(){
        LocalDateTime localDateTime1 = LocalDateTime.now().minusDays(1);
        LocalDateTime localDateTime2 = LocalDateTime.now().minusDays(2);
        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime1 = new WifiSpotVisitEndDateTime(localDateTime1);
        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime2 = new WifiSpotVisitEndDateTime(localDateTime2);
        assertThat(wifiSpotVisitEndDateTime1.compareTo(wifiSpotVisitEndDateTime2) > 0).isTrue();
    }

    @Test void WifiSpotVisitEndDateTimeSmallerTest(){
        LocalDateTime localDateTime1 = LocalDateTime.now().minusDays(2);
        LocalDateTime localDateTime2 = LocalDateTime.now().minusDays(1);
        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime1 = new WifiSpotVisitEndDateTime(localDateTime1);
        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime2 = new WifiSpotVisitEndDateTime(localDateTime2);
        assertThat(wifiSpotVisitEndDateTime1.compareTo(wifiSpotVisitEndDateTime2) < 0).isTrue();
    }


}
