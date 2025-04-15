package com.netquest.wifispotvisit.unit.service;

import com.netquest.domain.user.model.UserId;
import com.netquest.domain.user.service.UserService;
import com.netquest.domain.wifispot.service.impl.WifiSpotServiceImpl;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitHistoryDto;
import com.netquest.domain.wifispotvisit.mapper.WifiSpotVisitMapper;
import com.netquest.domain.wifispotvisit.service.impl.WifiSpotVisitServiceImpl;
import com.netquest.infrastructure.wifispotvisit.WifiSpotVisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WifiSpotVisitServiceImplTest {

    @InjectMocks
    private WifiSpotVisitServiceImpl wifiSpotVisitService;

    @InjectMocks
    private WifiSpotServiceImpl wifiSpotService;

    @Mock
    private UserService userService;

    @Mock
    private WifiSpotVisitRepository wifiSpotVisitRepository;

    @Mock
    private WifiSpotVisitMapper wifiSpotVisitMapper;

    private UUID userUUID;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userUUID = UUID.randomUUID();
    }

    @Test
    void testGetMyWifiSpotsVisits_NoVisits() {
        when(userService.existsById(userUUID)).thenReturn(true);
        when(wifiSpotVisitRepository.getMyWifiSpotsVisits(new UserId(userUUID), null, null, null))
                .thenReturn(Optional.empty());

        List<WifiSpotVisitHistoryDto> result = wifiSpotVisitService.getMyWifiSpotsVisits(userUUID, null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userService, times(1)).existsById(userUUID);
        verify(wifiSpotVisitRepository, times(1)).getMyWifiSpotsVisits(new UserId(userUUID), null, null, null);
        verifyNoInteractions(wifiSpotVisitMapper);
    }
}
