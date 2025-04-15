package com.netquest.controller.pointsearntransaction;


import com.netquest.domain.pointsearntransaction.dto.LeaderboardEntryDto;
import com.netquest.domain.pointsearntransaction.dto.PointsEarnTransactionDetailedDto;
import com.netquest.domain.pointsearntransaction.service.PointsEarnTransactionService;
import com.netquest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import static com.netquest.config.SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/points-earn")
public class PointsEarnTransactionController {

    private final PointsEarnTransactionService pointsEarnTransactionService;

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/leaderboard")
    public Page<LeaderboardEntryDto> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return pointsEarnTransactionService.getLeaderboard(pageable);
    }

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my-points-earn-transactions")
    public Page<PointsEarnTransactionDetailedDto> getMyPointsEarnTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        return pointsEarnTransactionService.getPointsEarnTransactionsByUserId(userDetails.getId(),pageable);
    }

    @Operation(security = {@SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my-total-points-earn-transactions")
    public Long getMyTotalPointsEarnTransactions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return pointsEarnTransactionService.getTotalPointsEarnTransactionByUserId(userDetails.getId());
    }
}
