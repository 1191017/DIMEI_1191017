package com.netquest.pointsearntransaction.unit.service;

import com.netquest.domain.pointsearntransaction.dto.*;
import com.netquest.domain.pointsearntransaction.mapper.PointsEarnTransactionMapper;
import com.netquest.domain.pointsearntransaction.model.PointsEarnTransaction;
import com.netquest.domain.pointsearntransaction.service.PointsEarnTransactionService;
import com.netquest.domain.pointsearntransaction.service.impl.PointsEarnTransactionServiceImpl;
import com.netquest.domain.user.exception.UserNotFoundException;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.user.service.UserService;
import com.netquest.infrastructure.pointsearntransaction.PointsEarnTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointsEarnTransactionEarnImplTest {

    @Mock
    private PointsEarnTransactionRepository pointsEarnTransactionRepository;

    @Mock
    private PointsEarnTransactionMapper pointsEarnTransactionMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private PointsEarnTransactionServiceImpl pointsEarnTransactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePointsEarnTransactionByVisit_ShouldSaveAndReturnDtoMy() {
        PointsEarnTransactionCreateByVisitDto createDto = new PointsEarnTransactionCreateByVisitDto();
        PointsEarnTransaction entity = new PointsEarnTransaction();
        PointsEarnTransactionDto dto = new PointsEarnTransactionDto();

        when(pointsEarnTransactionMapper.toNewEntityByVisit(createDto)).thenReturn(entity);
        when(pointsEarnTransactionRepository.save(entity)).thenReturn(entity);
        when(pointsEarnTransactionMapper.toDto(entity)).thenReturn(dto);

        PointsEarnTransactionDto result = pointsEarnTransactionService.savePointsEarnTransactionByMyVisit(createDto);

        assertEquals(dto, result);
        verify(pointsEarnTransactionRepository).save(entity);
    }

    @Test
    void getTotalPointsEarnTransactionByUserId_ShouldReturnTotalPoints() {
        UUID userId = UUID.randomUUID();
        UserId userIdObj = new UserId(userId);
        long totalPoints = 100;

        when(userService.existsById(userId)).thenReturn(true);
        when(pointsEarnTransactionRepository.getSumAmountByUserId(userIdObj)).thenReturn(totalPoints);

        Long result = pointsEarnTransactionService.getTotalPointsEarnTransactionByUserId(userId);

        assertEquals(totalPoints, result);
        verify(pointsEarnTransactionRepository).getSumAmountByUserId(userIdObj);
    }

    @Test
    void getTotalPointsEarnTransactionByUserId_ShouldThrowExceptionIfUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> pointsEarnTransactionService.getTotalPointsEarnTransactionByUserId(userId));

        verify(pointsEarnTransactionRepository, never()).getSumAmountByUserId(any());
    }

    @Test
    void getLeaderboard_ShouldReturnLeaderboardWithRanks() {
        PageRequest pageable = PageRequest.of(0, 10);
        TotalPointsEarnByUserDto dto1 = new TotalPointsEarnByUserDto(UUID.randomUUID(), 50L);
        TotalPointsEarnByUserDto dto2 = new TotalPointsEarnByUserDto(UUID.randomUUID(), 30L);

        Page<TotalPointsEarnByUserDto> mockPage = new PageImpl<>(List.of(dto1, dto2), pageable, 2);
        when(pointsEarnTransactionRepository.getLeaderboard(pageable)).thenReturn(mockPage);

        Page<LeaderboardEntryDto> result = pointsEarnTransactionService.getLeaderboard(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getRank());
        assertEquals(2, result.getContent().get(1).getRank());
    }

    @Test
    void getPointsEarnTransactionsByUserId_ShouldReturnTransactions() {
        UUID userId = UUID.randomUUID();
        PageRequest pageable = PageRequest.of(0, 10);
        PointsEarnTransaction entity = new PointsEarnTransaction();
        PointsEarnTransactionDetailedDto dto = new PointsEarnTransactionDetailedDto();

        when(userService.existsById(userId)).thenReturn(true);
        when(pointsEarnTransactionRepository.findByUserIdOrderByPointsEarnTransactionDateTimeDesc(
                new UserId(userId), pageable)).thenReturn(new PageImpl<>(List.of(entity)));
        when(pointsEarnTransactionMapper.toDetailedDto(entity)).thenReturn(dto);

        Page<PointsEarnTransactionDetailedDto> result = pointsEarnTransactionService.getPointsEarnTransactionsByUserId(userId, pageable);

        assertEquals(1, result.getTotalElements());
        //assertEquals(dto, result.getContent().getFirst());
    }

    @Test
    void getPointsEarnTransactionsByUserId_ShouldThrowExceptionIfUserNotFound() {
        UUID userId = UUID.randomUUID();
        PageRequest pageable = PageRequest.of(0, 10);

        when(userService.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> pointsEarnTransactionService.getPointsEarnTransactionsByUserId(userId, pageable));

        verify(pointsEarnTransactionRepository, never()).findByUserIdOrderByPointsEarnTransactionDateTimeDesc(any(), any());
    }
}
