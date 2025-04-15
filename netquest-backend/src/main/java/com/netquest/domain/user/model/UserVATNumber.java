package com.netquest.domain.user.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class UserVATNumber {
    private String vatNumber;

    public UserVATNumber(){

    }

    public UserVATNumber(String value){
        this.vatNumber = value;
    }
}
