package com.esecondhand.esecondhand.repository;

import com.esecondhand.esecondhand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Boolean existsByName(String name);
    Brand findByNameIgnoreCase(String name);
}
