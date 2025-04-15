package com.netquest.infrastructure.pointsearntransaction;

import com.netquest.domain.pointsearntransaction.dto.PointsEarnTransactionDto;
import com.netquest.domain.pointsearntransaction.dto.TotalPointsEarnByUserDto;
import com.netquest.domain.pointsearntransaction.model.PointsEarnTransaction;
import com.netquest.domain.pointsearntransaction.model.PointsEarnTransactionId;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpotId;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointsEarnTransactionRepository extends JpaRepository<PointsEarnTransaction, PointsEarnTransactionId> {

    @Query("SELECT new com.netquest.domain.pointsearntransaction.dto.TotalPointsEarnByUserDto(p.userId.value, SUM(p.pointsEarnTransactionAmount.value)) " +
            "FROM PointsEarnTransaction p " +
            " inner join User u on u.userId.value = p.userId.value " +
            " GROUP BY p.userId " +
            " ORDER BY SUM(p.pointsEarnTransactionAmount.value) DESC, u.username.userName desc")
    Page<TotalPointsEarnByUserDto> getLeaderboard(Pageable pageable);


    Page<PointsEarnTransaction> findByUserIdOrderByPointsEarnTransactionDateTimeDesc(UserId userId, Pageable pageable);

    @Query(" SELECT SUM(p.pointsEarnTransactionAmount.value) " +
            " from PointsEarnTransaction p " +
            " where p.userId = :userId")
    Long getSumAmountByUserId(UserId userId);

    @Query(" SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false end from PointsEarnTransaction  p " +
            " inner join Review r on r.reviewId = p.reviewId" +
            " where p.userId = :userId and r.wifiSpotId = :wifiSpotId ")
    boolean existsPointsEarnTransactionByUserIdAndWifiSpotId(UserId userId, WifiSpotId wifiSpotId);
}

