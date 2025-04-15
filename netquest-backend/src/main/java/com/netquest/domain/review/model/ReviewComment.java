package com.netquest.domain.review.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class ReviewComment {

    private final String value;
    public ReviewComment(String value) {
        if(value == null){
            value = "";
        }
        this.value = value.trim();
    }
    public ReviewComment() {
        this.value = "";
    }
}
