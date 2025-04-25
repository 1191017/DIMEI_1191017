package com.netquest.domain.wifispotvisit.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.netquest.infrastructure.wifispotvisit.WifiSpotVisitRepositoryCassandra;
import com.netquest.infrastructure.wifispotvisit.WifiSpotVisitRepositoryMongoDB;
import com.netquest.infrastructure.wifispotvisit.WifiSpotVisitRepositoryMySQL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final WifiSpotVisitRepositoryMySQL wifiSpotVisitRepositoryMySQL;
    private final WifiSpotVisitRepositoryMongoDB wifiSpotVisitRepositoryMongoDB;
    private final WifiSpotVisitRepositoryCassandra wifiSpotVisitRepositoryCassandra;


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

    public void importFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/app/scripts/mysql/wifi_spot_visit.csv", StandardCharsets.UTF_8))) {
            String header = reader.readLine(); // skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] c = line.split(","); // -1 para manter vazios

                WifiSpotVisitCreateDto dto = new WifiSpotVisitCreateDto();
                dto.setWifiSpotVisitId(UUID.fromString(c[0]));
                dto.setWifiSpotId(UUID.fromString(c[2]));
                dto.setStartDateTime(LocalDateTime.parse(c[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                dto.setEndDateTime(c[4].isBlank() ? null : LocalDateTime.parse(c[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                wifiSpotVisitRepositoryMySQL.createWifiSpotVisit(UUID.fromString(c[1]), dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar visitas do CSV: " + e.getMessage(), e);
        }
    }

    public void importFromJsonMongodb() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File("/app/scripts/mongodb/wifi_spot_visit.json"));

            for (JsonNode visitNode : root) {
                WifiSpotVisitCreateDto dto = new WifiSpotVisitCreateDto();
                dto.setWifiSpotVisitId(UUID.fromString(visitNode.get("_id").asText()));
                dto.setStartDateTime(LocalDateTime.parse(visitNode.get("start_time").asText()));
                dto.setEndDateTime(LocalDateTime.parse(visitNode.get("end_time").asText()));
                dto.setWifiSpotId(UUID.fromString(visitNode.get("wifi_spot").get("_id").asText()));

                wifiSpotVisitRepositoryMongoDB.createWifiSpotVisit(UUID.fromString(visitNode.get("user_id").asText()), dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar visitas do JSON: " + e.getMessage(), e);
        }
    }

    public void importFromCsvCassandra() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/app/scripts/cassandra/cassandra_wifi_spot_visit.csv", StandardCharsets.UTF_8))) {
            String header = reader.readLine(); // skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] c = line.split(",", -1);

                WifiSpotVisitCreateDto dto = new WifiSpotVisitCreateDto();
                dto.setWifiSpotVisitId(UUID.fromString(c[1]));              // visit_id
                dto.setWifiSpotId(UUID.fromString(c[2]));                   // wifi_spot_id
                dto.setStartDateTime(LocalDateTime.parse(c[(c.length-2)], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));            // visit_start
                dto.setEndDateTime(LocalDateTime.parse(c[(c.length-1)], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));              // visit_end

                wifiSpotVisitRepositoryCassandra.createWifiSpotVisit(UUID.fromString(c[0]), dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar visitas do CSV com metadata: " + e.getMessage(), e);
        }
    }
}
