package com.netquest.domain.review.model;


import com.netquest.domain.review.exception.ReviewAttributeClassificationNameEmpty;
import com.netquest.domain.review.exception.ReviewAttributeClassificationValueEmpty;
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
public class ReviewAttributeClassification {
    private final String name;
    private final String value;

    public ReviewAttributeClassification(String name, String value) {

        if(name == null || name.isEmpty()){
            throw new ReviewAttributeClassificationNameEmpty("Review Attribute Classification Name cannot be null or empty");
        }

        if(value == null || value.isEmpty()){
            throw new ReviewAttributeClassificationValueEmpty("Review Attribute Classification Value cannot be null or empty");
        }

        this.value = value.trim();
        this.name = name.trim();
    }
}
