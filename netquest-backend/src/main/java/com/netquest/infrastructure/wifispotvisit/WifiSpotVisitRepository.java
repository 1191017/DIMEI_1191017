package com.netquest.infrastructure.wifispotvisit;

import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisit;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WifiSpotVisitRepository extends JpaRepository<WifiSpotVisit, WifiSpotVisitId> {
    List<WifiSpotVisit> findByWifiSpotId(WifiSpotId wifiSpotId);
    List<WifiSpotVisit> findByUserId(UserId userId);
    List<WifiSpotVisit> findByWifiSpotIdAndUserId(WifiSpotId wifiSpotId, UserId userId);
    Optional<WifiSpotVisit> findByWifiSpotVisitIdAndUserId(WifiSpotVisitId wifiSpotVisitId, UserId userId);

    @Query("SELECT CASE WHEN COUNT(wsv) > 0 THEN TRUE ELSE FALSE END FROM WifiSpotVisit wsv " +
            "WHERE wsv.userId = :userId AND (" +
            "   (wsv.wifiSpotVisitStartDateTime.value < :startDateTime AND " +
            "   (wsv.wifiSpotVisitEndDateTime IS NULL OR :startDateTime < wsv.wifiSpotVisitEndDateTime.value)) " +
            "   OR " +
            "   (wsv.wifiSpotVisitStartDateTime.value < :endDateTime AND " +
            "   (wsv.wifiSpotVisitEndDateTime IS NULL OR :endDateTime < wsv.wifiSpotVisitEndDateTime.value)) " +
            "   OR " +
            "   (:startDateTime < wsv.wifiSpotVisitStartDateTime.value AND " +
            "   (wsv.wifiSpotVisitEndDateTime IS NULL OR wsv.wifiSpotVisitEndDateTime.value < :endDateTime))" +
            ")")
    boolean existsWifiSpotVisitConflictingIntervalByUserId(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("userId") UserId userId);



    @Query("SELECT CASE WHEN COUNT(wsv) > 0 THEN TRUE ELSE FALSE END from WifiSpotVisit wsv where wsv.wifiSpotVisitEndDateTime IS NULL and wsv.userId = :userId")
    boolean existsOnGoingWifiSpotVisitByUserId(@Param("userId") UserId userId);

    @Query("SELECT wsv from WifiSpotVisit wsv where wsv.wifiSpotVisitEndDateTime IS NULL and wsv.userId = :userId")
    Optional<WifiSpotVisit> findOngoingWifiSpotVisitByUserId(@Param("userId") UserId userId);

    @Query(value = "SELECT CASE WHEN COUNT(wsv) > 0 THEN TRUE ELSE FALSE END FROM WifiSpotVisit wsv " +
            "WHERE wsv.userId = :userId AND " +
            " wsv.wifiSpotId = :wifiSpotId AND " +
            " (wsv.wifiSpotVisitEndDateTime IS NULL OR " +
            " wsv.wifiSpotVisitEndDateTime.value >= :date10Minutes )")
    boolean existsWifiSpotVisitInSameWifiSpotInLast10MinutesByUserId(UserId userId, WifiSpotId wifiSpotId, LocalDateTime date10Minutes);

    @Query("SELECT wsv from WifiSpotVisit wsv where wsv.wifiSpotVisitEndDateTime IS NULL and wsv.userId = :userId")
    Optional<WifiSpotVisit> getOnGoingWifiSpotVisitByUserId(UserId userId);


    List<WifiSpotVisit> findByWifiSpotIdAndUserIdAndWifiSpotVisitEndDateTimeIsNotNull(WifiSpotId wifiSpotId, UserId userId);

    @Query("SELECT wsv from WifiSpotVisit wsv " +
            "inner join WifiSpot ws on ws.wifiSpotId = wsv.wifiSpotId " +
            "where wsv.userId = :userId " +
            "and (:wifiSpotName is null or lower(ws.wifiSpotName.value) like lower(:wifiSpotName))" +
            "and (:startDate is null or wsv.wifiSpotVisitStartDateTime.value >= :startDate)" +
            "and (:endDate is null or wsv.wifiSpotVisitEndDateTime.value <= :endDate)")
    Optional<List<WifiSpotVisit>> getMyWifiSpotsVisits(UserId userId, String wifiSpotName, LocalDateTime startDate, LocalDateTime endDate);
}
