package com.netquest.controller.wifispotvisit;


import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitHistoryDto;
import com.netquest.domain.wifispotvisit.service.WifiSpotVisitService;
import com.netquest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.netquest.config.SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/wifi-spot-visit")
public class WifiSpotVisitController {
    private final WifiSpotVisitService wifiSpotVisitService;

    @PostMapping("/mysql/create/{DATA_SIZE}")
    public ResponseEntity<String> importCsv(@PathVariable("DATA_SIZE") String dataSize) {
        wifiSpotVisitService.importFromCsv(dataSize);
        return ResponseEntity.ok("Wi-Fi spots visits importados com sucesso!");
    }

    @PostMapping("/mongodb/create/{DATA_SIZE}")
    public ResponseEntity<String> importCsvMongodb(@PathVariable("DATA_SIZE") String dataSize) {
        wifiSpotVisitService.importFromJsonMongodb(dataSize);
        return ResponseEntity.ok("Wi-Fi spots visits importados com sucesso!");
    }

    @PostMapping("/cassandra/create/{DATA_SIZE}")
    public ResponseEntity<String> importCsvCassandra(@PathVariable("DATA_SIZE") String dataSize) {
        wifiSpotVisitService.importFromCsvCassandra(dataSize);
        return ResponseEntity.ok("Wi-Fi spots visits importados com sucesso!");
    }

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/start/{wifi-spot-id}")
    public WifiSpotVisitDto startVisit(@PathVariable(name = "wifi-spot-id") UUID wifiSpotUUID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return wifiSpotVisitService.startVisit(userDetails.getId(),wifiSpotUUID);
    }

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/finish/{id}")
    public WifiSpotVisitDto finishVisit(
            @PathVariable(name = "id") UUID wifiSpotVisitUUID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return wifiSpotVisitService.finishVisit(userDetails.getId() ,wifiSpotVisitUUID);
    }

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ongoing")
    public WifiSpotVisitDto getWifiSpotVisitOngoing() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return wifiSpotVisitService.getWifiSpotVisitOngoing(userDetails.getId());
    }

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my-visits")
    public List<WifiSpotVisitHistoryDto> getMyWifiSpotsVisits(
            @RequestParam(required = false) String wifiSpotName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return wifiSpotVisitService.getMyWifiSpotsVisits(userDetails.getId(), wifiSpotName, startDate, endDate);
    }

}
