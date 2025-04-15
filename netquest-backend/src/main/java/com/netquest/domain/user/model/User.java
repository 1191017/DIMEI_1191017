package com.netquest.domain.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    })
    private UserId userId;

    @Embedded
    private UserFirstName firstName;

    @Embedded
    private UserLastName lastName;

    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Embedded
    private Username username;

    @Embedded
    private UserMail email;

    @Embedded
    private UserPassword password;

    @Embedded
    private UserBirthDate birthDate;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Embedded
    private UserAddress address;

    @Embedded
    private UserVATNumber vatNumber;

    public User(UserFirstName firstName, UserLastName lastName, UserGender gender, Username username,
                UserPassword password, UserMail email, UserBirthDate birthDate, UserRole role,
                UserAddress address, UserVATNumber vatNumber) {
        this.userId = new UserId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.role = role;
        this.address = address;
        this.vatNumber = vatNumber;
    }


}
