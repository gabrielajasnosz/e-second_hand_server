package com.esecondhand.esecondhand.domain.repository;


import com.esecondhand.esecondhand.domain.entity.Category;
import com.esecondhand.esecondhand.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDisplayNameIgnoreCaseContaining(String keyword);

    User findByDisplayName(String displayName);
    User findByEmail(String email);

    Boolean existsByEmail(String email);
}


