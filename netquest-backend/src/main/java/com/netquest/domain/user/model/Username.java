package com.netquest.domain.user.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Value
@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class Username {
    String userName;

    public Username() {
        this.userName = "";
    }

    public Username(String value) {
        this.userName = value;
    }
}
