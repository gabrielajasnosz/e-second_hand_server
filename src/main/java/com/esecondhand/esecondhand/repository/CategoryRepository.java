package com.esecondhand.esecondhand.repository;

import com.esecondhand.esecondhand.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c FROM Category c where c.parentId is null")
    List<Category> findAllCategories();

}
