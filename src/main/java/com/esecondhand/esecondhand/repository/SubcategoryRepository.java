package com.esecondhand.esecondhand.repository;

import com.esecondhand.esecondhand.domain.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {

    List<Subcategory> findAllByMainCategoryId(int mainCategoryId);

}
