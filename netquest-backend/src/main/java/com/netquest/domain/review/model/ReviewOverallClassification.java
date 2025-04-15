package com.netquest.domain.review.model;

import com.netquest.domain.review.exception.ReviewOverallClassificationIllegalValue;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class ReviewOverallClassification {
    private final int value;

    public ReviewOverallClassification(int value) {
        if(value < 0 || value > 5){
            throw new ReviewOverallClassificationIllegalValue("Review Overall Classification must be between 0 and 5");
        }
        this.value = value;
    }
}
