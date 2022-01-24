package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Follower;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    @Query("SELECT v FROM VerificationToken v inner join User u on u.id=v.user.id WHERE v.expiryDate <= ?1 and u.enabled = false")
    List<VerificationToken> findUnconfirmedUsers(LocalDateTime localDateTime);
}
