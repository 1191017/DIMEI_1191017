package com.netquest.domain.review.mapper;

import com.netquest.domain.review.dto.ReviewAttributeClassificationDto;
import com.netquest.domain.review.dto.ReviewCreateDto;
import com.netquest.domain.review.dto.ReviewDetailedDto;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.model.Review;
import com.netquest.domain.review.model.ReviewAttributeClassification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public interface ReviewMapper {
    Review toNewEntity(ReviewCreateDto reviewCreateDto, UUID userUUID);
    ReviewAttributeClassification toAttributeClassificationEntity(ReviewAttributeClassificationDto reviewAttributeClassificationDto);
    ReviewDto toDto(Review review);
    ReviewAttributeClassificationDto toAttributeClassificationDto(ReviewAttributeClassification reviewAttributeClassification);
    ReviewDetailedDto toDetailedDto(Review review);
}
