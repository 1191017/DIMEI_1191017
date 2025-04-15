package com.netquest.domain.wifispotvisit.service.impl;

import com.netquest.domain.pointsearntransaction.dto.PointsEarnTransactionCreateByVisitDto;
import com.netquest.domain.pointsearntransaction.dto.PointsEarnTransactionCreateByVisitMySpotDto;
import com.netquest.domain.pointsearntransaction.service.PointsEarnTransactionService;
import com.netquest.domain.user.exception.UserNotFoundException;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.user.service.UserService;
import com.netquest.domain.wifispot.dto.WifiSpotDto;
import com.netquest.domain.wifispot.exception.WifiSpotNotFoundException;
import com.netquest.domain.wifispot.model.WifiSpot;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispot.service.WifiSpotService;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitCreateDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitHistoryDto;
import com.netquest.domain.wifispotvisit.exception.*;
import com.netquest.domain.wifispotvisit.mapper.WifiSpotVisitMapper;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisit;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitEndDateTime;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitId;
import com.netquest.domain.wifispotvisit.service.WifiSpotVisitService;
import com.netquest.infrastructure.wifispotvisit.WifiSpotVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WifiSpotVisitServiceImpl implements WifiSpotVisitService {
    private final WifiSpotVisitRepository wifiSpotVisitRepository;
    private final WifiSpotVisitMapper wifiSpotVisitMapper;
    private final PointsEarnTransactionService pointsEarnTransactionService;
    private final UserService userService;
    private final WifiSpotService wifiSpotService;


    @Override
    public WifiSpotVisitDto saveWifiSpotVisit(UUID userUUID, WifiSpotVisitCreateDto wifiSpotVisitCreateDto) {
        WifiSpotVisit wifiSpotVisit = wifiSpotVisitMapper.toNewEntity(userUUID,wifiSpotVisitCreateDto);


        if(!userService.existsById(userUUID)){
            throw new UserNotFoundException("User not found");
        }

        UserId userId = new UserId(userUUID);
        if(wifiSpotVisitRepository.existsOnGoingWifiSpotVisitByUserId(userId)){
            throw new WifiSpotVisitOngoingException("Wifi spot visit ongoing for this user");
        }

        if(wifiSpotVisitRepository.existsWifiSpotVisitConflictingIntervalByUserId(
                wifiSpotVisitCreateDto.getStartDateTime(),
                wifiSpotVisitCreateDto.getEndDateTime(),
                userId
        )){
            throw new WifiSpotVisitDatesConflictException("There is a conflict of dates to another visit for this user");
        }
        WifiSpotId wifiSpotId = new WifiSpotId(wifiSpotVisitCreateDto.getWifiSpotId());
        LocalDateTime wifiSpotVisitStartDate10MinutesAgo = wifiSpotVisitCreateDto.getStartDateTime().minusMinutes(10);
        if(wifiSpotVisitRepository.existsWifiSpotVisitInSameWifiSpotInLast10MinutesByUserId(userId,wifiSpotId,wifiSpotVisitStartDate10MinutesAgo)){
            throw new WifiSpotVisitInSameWifiSpotInLast10Minutes("There is already a visit in the same wifi spot in last 10 minutes");
        }

        WifiSpotVisitDto wifiSpotVisitDto = wifiSpotVisitMapper.toDto(wifiSpotVisitRepository.save(wifiSpotVisit));
        createPointsEarnTransactionBasedOnVisit(wifiSpotVisitDto);
        return wifiSpotVisitDto;

    }

    @Override
    public WifiSpotVisitDto finishVisit(UUID userUUID, UUID wifiSpotVisitUUID) {
        WifiSpotVisitId wifiSpotVisitId = new WifiSpotVisitId(wifiSpotVisitUUID);
        UserId userId = new UserId(userUUID);
        WifiSpotVisit wifiSpotVisit = wifiSpotVisitRepository.findByWifiSpotVisitIdAndUserId(wifiSpotVisitId,userId)
                .orElseThrow(() -> new WifiSpotVisitNotFoundException("Wifi spot visit not found"));


        if(wifiSpotVisit.getWifiSpotVisitEndDateTime() != null){
            throw new WifiSpotVisitEndDateTimeAlreadyFilledException("Wifi spot visit end date time already filled");
        }



        WifiSpotVisitEndDateTime wifiSpotVisitEndDateTime = new WifiSpotVisitEndDateTime(LocalDateTime.now());
        wifiSpotVisit.updateEndDateTime(wifiSpotVisitEndDateTime);

        WifiSpotVisitDto wifiSpotVisitDto = wifiSpotVisitMapper.toDto(wifiSpotVisitRepository.save(wifiSpotVisit));
        createPointsEarnTransactionBasedOnVisit(wifiSpotVisitDto);
        return wifiSpotVisitDto;
    }

    @Override
    public boolean hasUserVisitedWifiSpotBasedOnMinutes(UUID userUUID, UUID wifiSpotUUID,long minutes) {
        if(!userService.existsById(userUUID)){
            throw new UserNotFoundException("User not found");
        }

        List<WifiSpotVisit> listWifiSpotVisits = wifiSpotVisitRepository.findByWifiSpotIdAndUserIdAndWifiSpotVisitEndDateTimeIsNotNull(new WifiSpotId(wifiSpotUUID),new UserId(userUUID));
        long totalMinutes = listWifiSpotVisits.stream()
                .mapToLong(visit -> Duration.between(visit.getWifiSpotVisitStartDateTime().getValue(), visit.getWifiSpotVisitEndDateTime().getValue()).toMinutes())
                .sum();
        return totalMinutes >= minutes;
    }

    @Override
    public WifiSpotVisitDto getWifiSpotVisitOngoing(UUID userUUID) {
        if(!userService.existsById(userUUID)){
            throw new UserNotFoundException("User not found");
        }
        UserId userId = new UserId(userUUID);
        WifiSpotVisit wifiSpotVisit = wifiSpotVisitRepository.getOnGoingWifiSpotVisitByUserId(userId).orElseThrow(
                () -> new WifiSpotVisitNotFoundException("There is not any wifi spot visit ongoing"));
        return wifiSpotVisitMapper.toDto(wifiSpotVisit);

    }

    @Override
    public WifiSpotVisitDto startVisit(UUID userUUID, UUID wifiSpotUUID) {
        WifiSpotVisitCreateDto wifiSpotVisitCreateDto = new WifiSpotVisitCreateDto();
        wifiSpotVisitCreateDto.setWifiSpotId(wifiSpotUUID);
        wifiSpotVisitCreateDto.setStartDateTime(LocalDateTime.now());
        return saveWifiSpotVisit(userUUID, wifiSpotVisitCreateDto) ;
    }
    private void createPointsEarnTransactionBasedOnVisit(WifiSpotVisitDto wifiSpotVisitDto) {
        createPointsEarnTransactionBasedOnMyVisit(wifiSpotVisitDto);
        createPointsEarnTransactionBasedOnVisitMySpot(wifiSpotVisitDto);
    }

    private void createPointsEarnTransactionBasedOnMyVisit(WifiSpotVisitDto wifiSpotVisitDto) {
        if(wifiSpotVisitDto.endDateTime() == null) {
            return;
        }
        pointsEarnTransactionService.savePointsEarnTransactionByMyVisit(
                new PointsEarnTransactionCreateByVisitDto(
                        wifiSpotVisitDto.startDateTime(), wifiSpotVisitDto.endDateTime(), wifiSpotVisitDto.userId(), wifiSpotVisitDto.id()
                )
        );

    }

    private void createPointsEarnTransactionBasedOnVisitMySpot(WifiSpotVisitDto wifiSpotVisitDto){

        if(wifiSpotVisitDto.endDateTime() == null) {
            return;
        }
        if(wifiSpotVisitDto.startDateTime() == null) {
            return;
        }

        if(ChronoUnit.MINUTES.between(wifiSpotVisitDto.startDateTime(), wifiSpotVisitDto.endDateTime()) < 10){
            return;
        }

        WifiSpotDto wifiSpotDto;


    }

    @Override
    public List<WifiSpotVisitHistoryDto> getMyWifiSpotsVisits(UUID userUUID, String wifiSpotName, LocalDateTime startDate, LocalDateTime endDate) {
        List<WifiSpotVisitHistoryDto> wifiSpotVisitHistoryDtos = new ArrayList<>();
        if(!userService.existsById(userUUID)){
            throw new UserNotFoundException("User not found");
        }
        UserId userId = new UserId(userUUID);
        List<WifiSpotVisit> wifiSpotVisit = wifiSpotVisitRepository.getMyWifiSpotsVisits(userId, wifiSpotName != null ? "%" + wifiSpotName + "%" : null, startDate, endDate).orElse(new ArrayList<>());
        List<WifiSpotVisitDto> wifiSpotVisitDtoList = wifiSpotVisit.stream().map(wifiSpotVisitMapper::toDto).collect(Collectors.toList());
        for (WifiSpotVisitDto wifiSpotVisitDto: wifiSpotVisitDtoList) {

        }
        return  wifiSpotVisitHistoryDtos;
    }

}
