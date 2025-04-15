package com.netquest.domain.review.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class ReviewId {
    private final UUID value;

    public ReviewId(UUID value) {
        this.value = value;
    }

    public ReviewId() {
        this.value = UUID.randomUUID();
    }
}
