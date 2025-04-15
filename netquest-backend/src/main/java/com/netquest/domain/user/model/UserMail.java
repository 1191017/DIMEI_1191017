package com.netquest.domain.user.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class UserMail {
    String mail;

    public UserMail() {
        this.mail = "";
    }

    public UserMail(String value) {
        this.mail = value;
    }


}


