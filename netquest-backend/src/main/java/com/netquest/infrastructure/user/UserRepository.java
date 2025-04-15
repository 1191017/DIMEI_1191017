package com.netquest.infrastructure.user;

import com.netquest.domain.user.model.User;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.user.model.UserMail;
import com.netquest.domain.user.model.Username;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {

    Optional<User> findByUsername(Username username);

    Optional<User> findById(UserId id);

    boolean existsByUsername(Username username);

    boolean existsByEmail(UserMail email);

    @Modifying
    @Transactional
    @Query("UPDATE User u " +
            "SET " +
            "u.password.password = CASE WHEN :password IS NOT NULL THEN :password ELSE u.password.password END, " +
            "u.email.mail = CASE WHEN :email IS NOT NULL THEN :email ELSE u.email.mail END, " +
            "u.vatNumber.vatNumber = CASE WHEN :vatNumber IS NOT NULL THEN :vatNumber ELSE u.vatNumber.vatNumber END, " +
            "u.address.addressLine1 = CASE WHEN :addressLine1 IS NOT NULL THEN :addressLine1 ELSE u.address.addressLine1 END, " +
            "u.address.addressLine2 = CASE WHEN :addressLine2 IS NOT NULL THEN :addressLine2 ELSE u.address.addressLine2 END, " +
            "u.address.city = CASE WHEN :addressCity IS NOT NULL THEN :addressCity ELSE u.address.city END, " +
            "u.address.district = CASE WHEN :addressDistrict IS NOT NULL THEN :addressDistrict ELSE u.address.district END, " +
            "u.address.country = CASE WHEN :addressCountry IS NOT NULL THEN :addressCountry ELSE u.address.country END, " +
            "u.address.zipCode = CASE WHEN :addressZipCode IS NOT NULL THEN :addressZipCode ELSE u.address.zipCode END " +
            "WHERE u.id.value = :id")
    void updateUser(
            @Param("id") UUID id,
            @Param("password") String password,
            @Param("email") String email,
            @Param("vatNumber") String vatNumber,
            @Param("addressLine1") String addressLine1,
            @Param("addressLine2") String addressLine2,
            @Param("addressCity") String addressCity,
            @Param("addressDistrict") String addressDistrict,
            @Param("addressCountry") String addressCountry,
            @Param("addressZipCode") String addressZipCode
    );

    
}
