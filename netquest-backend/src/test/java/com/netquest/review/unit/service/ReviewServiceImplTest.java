package com.netquest.review.unit.service;

import com.netquest.domain.review.dto.ReviewAttributeClassificationDto;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.mapper.ReviewMapper;
import com.netquest.domain.review.model.*;
import com.netquest.domain.review.service.impl.ReviewServiceImpl;
import com.netquest.domain.user.exception.UserNotFoundException;
import com.netquest.domain.user.model.User;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.user.model.Username;
import com.netquest.domain.user.service.UserService;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispot.service.impl.WifiSpotServiceImpl;
import com.netquest.infrastructure.review.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private UserService userService;

    @Mock
    private WifiSpotServiceImpl wifiSpotService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    private UUID userUUID;
    private UUID wifiSpotUUID;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userUUID = UUID.randomUUID();
        wifiSpotUUID = UUID.randomUUID();
    }


}
