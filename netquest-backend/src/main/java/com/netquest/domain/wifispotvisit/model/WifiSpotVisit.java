package com.netquest.domain.wifispotvisit.model;

import com.netquest.domain.user.model.User;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpot;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.exception.WifiSpotVisitEndDateTimeBeforeWifiSpotVisitStartDateTimeException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "wifi_spot_visit")
public class WifiSpotVisit {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_visit_id"))
    })
    private WifiSpotVisitId wifiSpotVisitId;

    @Embedded
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_visit_start_datetime"))
    })
    private WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime;

    @Embedded
    @Temporal(TemporalType.TIMESTAMP)
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_visit_end_datetime"))
    })
    private WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_visit_wifi_spot_id"))
    })
    private WifiSpotId wifiSpotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wifi_spot_visit_wifi_spot_id", insertable = false, updatable = false)
    private WifiSpot wifiSpot;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_visit_user_id"))
    })
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wifi_spot_visit_user_id", insertable = false, updatable = false)
    private User user;


    public WifiSpotVisit(
            @NonNull WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime
            ,WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime
            ,@NonNull WifiSpotId wifiSpotId
            ,@NonNull UserId userId
    ) {
       validateEndDateBeforeStartDate(wifiSpotVisitStartDateTime, wifiSpotVisitEndDateTime);

        this.wifiSpotVisitId = new WifiSpotVisitId();
        this.wifiSpotVisitStartDateTime = new WifiSpotVisitStartDateTime(wifiSpotVisitStartDateTime.getValue());
        this.wifiSpotVisitEndDateTime = wifiSpotVisitEndDateTime != null ? new WifiSpotVisitEndDateTime(wifiSpotVisitEndDateTime.getValue()) : null;
        this.wifiSpotId = new WifiSpotId(wifiSpotId.getValue());
        this.userId = new UserId(userId.getValue());
    }

    public void updateEndDateTime(WifiSpotVisitEndDateTime endDateTime){
        validateEndDateBeforeStartDate(this.wifiSpotVisitStartDateTime, endDateTime);
        this.wifiSpotVisitEndDateTime = new WifiSpotVisitEndDateTime(endDateTime.getValue());
    }

    private void validateEndDateBeforeStartDate(WifiSpotVisitStartDateTime wifiSpotVisitStartDateTime, WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime){
        if(
                wifiSpotVisitEndDateTime != null &&
                wifiSpotVisitEndDateTime.getValue() != null &&
                isEndDateBeforeStartDate(wifiSpotVisitStartDateTime, wifiSpotVisitEndDateTime)
        ) {
            throw new WifiSpotVisitEndDateTimeBeforeWifiSpotVisitStartDateTimeException("End date time cannot be before start date");
        }
    }

    private boolean isEndDateBeforeStartDate(WifiSpotVisitStartDateTime start, WifiSpotVisitEndDateTime end){
        return end.getValue().isBefore(start.getValue());
    }

}
