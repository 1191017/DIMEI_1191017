package com.netquest.domain.user.model;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class UserId {
    private final UUID value;

    public UserId(UUID value) {
        this.value = value;
    }

    public UserId() {
        this.value = UUID.randomUUID();
    }

}
