package com.esecondhand.esecondhand.domain.repository;


import com.esecondhand.esecondhand.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByDisplayName(String displayName);
    User findByEmail(String email);

    Boolean existsByEmail(String email);
}


