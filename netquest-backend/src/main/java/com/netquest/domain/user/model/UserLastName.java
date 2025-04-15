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
public class UserLastName {
    String lastName;

    public UserLastName() {
        this.lastName = "";
    }

    public UserLastName(String value) {
        this.lastName = value;
    }
}
