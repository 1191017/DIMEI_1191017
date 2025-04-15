package com.netquest.domain.user.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class UserPassword {
    String password;

    public UserPassword() {
        this.password = "";
    }

    public UserPassword(String value) {
        this.password = value;
    }

}


