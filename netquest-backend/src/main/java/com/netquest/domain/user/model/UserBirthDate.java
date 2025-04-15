package com.netquest.domain.user.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Value
@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class UserBirthDate {
    LocalDate birthdate;

    public UserBirthDate() {
        this.birthdate = LocalDate.now();
    }

    public UserBirthDate( LocalDate value){
        this.birthdate = value;
    }

    public UserBirthDate(String value) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.birthdate = LocalDate.parse(value, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is yyyy-MM-dd.");
        }
    }
}

