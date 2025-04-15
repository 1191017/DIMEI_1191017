package com.netquest.domain.wifispot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
@Entity
@Table(name = "wifi_spot_address")
public class WifiSpotAddress {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_address_id"))
    })
    private final WifiSpotAddressId wifiSpotAddressId;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_address_country", nullable = false))
    })
    private final WifiSpotAddressCountry wifiSpotAddressCountry;
    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_address_zip_code", nullable = false))
    })
    private final WifiSpotAddressZipCode wifiSpotAddressZipCode;
    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_address_line_1", nullable = false))
    })
    private final WifiSpotAddressLine1 wifiSpotAddressLine1;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_address_line_2"))
    })
    private final WifiSpotAddressLine2 wifiSpotAddressLine2;
    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_address_city", nullable = false))
    })
    private final WifiSpotAddressCity wifiSpotAddressCity;
    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_address_district", nullable = false))
    })
    private final WifiSpotAddressDistrict wifiSpotAddressDistrict;

    public WifiSpotAddress(WifiSpotAddressCountry wifiSpotAddressCountry, WifiSpotAddressZipCode wifiSpotAddressZipCode, WifiSpotAddressLine1 wifiSpotAddressLine1, WifiSpotAddressLine2 wifiSpotAddressLine2, WifiSpotAddressCity wifiSpotAddressCity, WifiSpotAddressDistrict wifiSpotAddressDistrict) {
        this.wifiSpotAddressId = new WifiSpotAddressId();
        this.wifiSpotAddressCountry = wifiSpotAddressCountry;
        this.wifiSpotAddressZipCode = wifiSpotAddressZipCode;
        this.wifiSpotAddressLine1 = wifiSpotAddressLine1;
        this.wifiSpotAddressLine2 = wifiSpotAddressLine2;
        this.wifiSpotAddressCity = wifiSpotAddressCity;
        this.wifiSpotAddressDistrict = wifiSpotAddressDistrict;
    }

    public WifiSpotAddress(WifiSpotAddressId wifiSpotAddressId, WifiSpotAddressCountry wifiSpotAddressCountry, WifiSpotAddressZipCode wifiSpotAddressZipCode, WifiSpotAddressLine1 wifiSpotAddressLine1, WifiSpotAddressLine2 wifiSpotAddressLine2, WifiSpotAddressCity wifiSpotAddressCity, WifiSpotAddressDistrict wifiSpotAddressDistrict) {
        this.wifiSpotAddressId = wifiSpotAddressId;
        this.wifiSpotAddressCountry = wifiSpotAddressCountry;
        this.wifiSpotAddressZipCode = wifiSpotAddressZipCode;
        this.wifiSpotAddressLine1 = wifiSpotAddressLine1;
        this.wifiSpotAddressLine2 = wifiSpotAddressLine2;
        this.wifiSpotAddressCity = wifiSpotAddressCity;
        this.wifiSpotAddressDistrict = wifiSpotAddressDistrict;
    }
}
