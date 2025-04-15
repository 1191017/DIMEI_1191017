package com.netquest.domain.pointsearntransaction.mapper.impl;

import com.netquest.domain.pointsearntransaction.dto.*;
import com.netquest.domain.pointsearntransaction.mapper.PointsEarnTransactionMapper;
import com.netquest.domain.pointsearntransaction.model.PointsEarnTransaction;
import com.netquest.domain.review.mapper.ReviewMapper;
import com.netquest.domain.review.model.ReviewId;
import com.netquest.domain.user.mapper.UserMapper;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.mapper.WifiSpotMapper;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.mapper.WifiSpotVisitMapper;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PointsEarnTransactionMapperImpl implements PointsEarnTransactionMapper {

    private final UserMapper userMapper;
    private final WifiSpotMapper wifiSpotMapper;
    private final WifiSpotVisitMapper wifiSpotVisitMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public PointsEarnTransactionDto toDto(PointsEarnTransaction pointsEarnTransaction) {


        UUID id = pointsEarnTransaction.getPointsEarnTransactionId().getValue();
        LocalDateTime dateTime = pointsEarnTransaction.getPointsEarnTransactionDateTime().getValue();
        long amount = pointsEarnTransaction.getPointsEarnTransactionAmount().getValue();
        UUID userId = pointsEarnTransaction.getUserId().getValue();

        UUID wifiSpotId = pointsEarnTransaction.getWifiSpotId() == null ? null : pointsEarnTransaction.getWifiSpotId().getValue();
        UUID wifiSpotVisitId = pointsEarnTransaction.getWifiSpotVisitId() == null ? null : pointsEarnTransaction.getWifiSpotVisitId().getValue();


        return new PointsEarnTransactionDto(
                id,dateTime,amount,userId,wifiSpotId,wifiSpotVisitId
        );
    }

    @Override
    public PointsEarnTransactionDetailedDto toDetailedDto(PointsEarnTransaction pointsEarnTransaction) {
        UUID id = pointsEarnTransaction.getPointsEarnTransactionId().getValue();
        LocalDateTime dateTime = pointsEarnTransaction.getPointsEarnTransactionDateTime().getValue();
        long amount = pointsEarnTransaction.getPointsEarnTransactionAmount().getValue();

        return new PointsEarnTransactionDetailedDto(
                id,
                dateTime,
                amount,
                userMapper.toUserDto(pointsEarnTransaction.getUser()),
                wifiSpotMapper.wifiSpotDomainToDto(pointsEarnTransaction.getWifiSpot()),
                wifiSpotVisitMapper.toDetailedDto(pointsEarnTransaction.getWifiSpotVisit()),
                wifiSpotVisitMapper.toDetailedDto(pointsEarnTransaction.getWifiSpotVisitMySpot()),
                reviewMapper.toDetailedDto(pointsEarnTransaction.getReview())
        );

    }

    @Override
    public PointsEarnTransaction toNewEntityByVisit(PointsEarnTransactionCreateByVisitDto pointsEarnTransactionCreateByVisitDto) {
        return PointsEarnTransaction.createPointsEarnTransactionBasedOnMyVisit(
                new UserId(pointsEarnTransactionCreateByVisitDto.getUserId()),
                new WifiSpotVisitId(pointsEarnTransactionCreateByVisitDto.getWifiSpotVisitId()),
                pointsEarnTransactionCreateByVisitDto.getStartDateTime(),
                pointsEarnTransactionCreateByVisitDto.getEndDateTime()
        );
    }

    @Override
    public PointsEarnTransaction toNewEntityByWifiSpotCreation(PointsEarnTransactionCreateByWifiSpotCreationDto pointsEarnTransactionCreateByWifiSpotCreationDto) {
        return PointsEarnTransaction.createPointsEarnTransactionBasedOnSpotCreation(
                new UserId(pointsEarnTransactionCreateByWifiSpotCreationDto.getUserId()),
                new WifiSpotId(pointsEarnTransactionCreateByWifiSpotCreationDto.getWifiSpotId())
        );
    }

    @Override
    public PointsEarnTransaction toNewEntityByVisitMySpot(PointsEarnTransactionCreateByVisitMySpotDto pointsEarnTransactionCreateByVisitMySpotDto) {
        return PointsEarnTransaction.createPointsEarnTransactionBasedOnVisitingMySpot(
                new UserId(pointsEarnTransactionCreateByVisitMySpotDto.getUserId()),
                new WifiSpotVisitId(pointsEarnTransactionCreateByVisitMySpotDto.getWifiSpotVisitId()),
                pointsEarnTransactionCreateByVisitMySpotDto.getStartDateTime(),
                pointsEarnTransactionCreateByVisitMySpotDto.getEndDateTime()
        );
    }

    @Override
    public PointsEarnTransaction toNewEntityByReview(PointsEarnTransactionCreateByReviewDto pointsEarnTransactionCreateByReviewDto) {
        return PointsEarnTransaction.createPointsEarnTransactionBasedOnReview(
                new UserId(pointsEarnTransactionCreateByReviewDto.getUserUUID()),
                new ReviewId(pointsEarnTransactionCreateByReviewDto.getReviewUUID())
        );
    }
}
