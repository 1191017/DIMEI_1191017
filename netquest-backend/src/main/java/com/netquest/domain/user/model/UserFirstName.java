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
public class UserFirstName {
    String firstName;

    public UserFirstName() {
        this.firstName = "";
    }

    public UserFirstName(String value) {
        this.firstName = value;
    }
}
