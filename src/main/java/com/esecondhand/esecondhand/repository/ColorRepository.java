package com.esecondhand.esecondhand.repository;

import com.esecondhand.esecondhand.domain.Brand;
import com.esecondhand.esecondhand.domain.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {


}
