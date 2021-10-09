package com.esecondhand.esecondhand.repository;


import com.esecondhand.esecondhand.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByDisplayName(String displayName);
    User findByEmail(String email);

    Boolean existsByEmail(String email);
}


