package com.netquest.wifispotvisit.unit;

import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.exception.WifiSpotVisitEndDateTimeBeforeWifiSpotVisitStartDateTimeException;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisit;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitEndDateTime;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitStartDateTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class WifSpotVisitTest {
    @Test
    public void WifiSpotVisitIdGoodTest(){

        WifiSpotVisitStartDateTime startDateTime = new WifiSpotVisitStartDateTime(LocalDateTime.now().minusDays(2));
        WifiSpotVisitEndDateTime endDateTime = new WifiSpotVisitEndDateTime(startDateTime.getValue().plusHours(2));
        WifiSpotId wifiSpotId = new WifiSpotId();
        UserId userId = new UserId();

        WifiSpotVisit wifiSpotVisit = new WifiSpotVisit(startDateTime, endDateTime, wifiSpotId, userId);
        assertThat(wifiSpotVisit.getWifiSpotVisitId()).isNotNull();
        assertThat(wifiSpotVisit.getWifiSpotVisitStartDateTime()).isEqualTo(startDateTime);
        assertThat(wifiSpotVisit.getWifiSpotVisitEndDateTime()).isEqualTo(endDateTime);
        assertThat(wifiSpotVisit.getWifiSpotId()).isEqualTo(wifiSpotId);
        assertThat(wifiSpotVisit.getUserId()).isEqualTo(userId);
    }

    @Test
    public void WifiSpotVisitNullTest(){

        NullPointerException exception = catchThrowableOfType(
                () ->
                        new WifiSpotVisit(null, null, null, null),
                NullPointerException.class
        );

        assertThat(exception.getMessage()).isNotEmpty();

    }

    @Test
    public void WifiSpotVisitEndDateTimeNullTest(){
        WifiSpotVisitStartDateTime startDateTime = new WifiSpotVisitStartDateTime(LocalDateTime.now().minusDays(2));
        WifiSpotVisitEndDateTime endDateTime = null;
        WifiSpotId wifiSpotId = new WifiSpotId();
        UserId userId = new UserId();

        WifiSpotVisit wifiSpotVisit = new WifiSpotVisit(startDateTime, endDateTime, wifiSpotId, userId);
        assertThat(wifiSpotVisit.getWifiSpotVisitId()).isNotNull();
        assertThat(wifiSpotVisit.getWifiSpotVisitStartDateTime()).isEqualTo(startDateTime);
        assertThat(wifiSpotVisit.getWifiSpotVisitEndDateTime()).isEqualTo(endDateTime);
        assertThat(wifiSpotVisit.getWifiSpotVisitEndDateTime()).isNull();
        assertThat(wifiSpotVisit.getWifiSpotId()).isEqualTo(wifiSpotId);
        assertThat(wifiSpotVisit.getUserId()).isEqualTo(userId);

    }

    @Test
    public void WifiSpotVisitEndDateBeforeStartDateTest(){

        WifiSpotVisitEndDateTime endDateTime = new WifiSpotVisitEndDateTime(LocalDateTime.now().minusDays(2));
        WifiSpotVisitStartDateTime startDateTime = new WifiSpotVisitStartDateTime(endDateTime.getValue().plusHours(2));

        WifiSpotId wifiSpotId = new WifiSpotId();
        UserId userId = new UserId();

        WifiSpotVisitEndDateTimeBeforeWifiSpotVisitStartDateTimeException exception = catchThrowableOfType(
                () ->
                        new WifiSpotVisit(startDateTime, endDateTime, wifiSpotId, userId),
                WifiSpotVisitEndDateTimeBeforeWifiSpotVisitStartDateTimeException.class
        );

        assertThat(exception.getMessage()).isNotEmpty();

    }

}
