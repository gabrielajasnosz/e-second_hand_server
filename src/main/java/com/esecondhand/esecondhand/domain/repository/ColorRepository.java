package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findById(Long id);

    Color findByNameIgnoreCase(String name);

}
