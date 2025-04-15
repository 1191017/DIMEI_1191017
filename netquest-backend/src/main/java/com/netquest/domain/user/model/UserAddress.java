package com.netquest.domain.user.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@Data
public class UserAddress {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String district;
    private String country;
    private String zipCode;

    public UserAddress(String addressLine1, String addressLine2, String city, String district, String country, String zipCode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.district = district;
        this.country = country;
        this.zipCode = zipCode;
    }

    public UserAddress() {

    }
}
