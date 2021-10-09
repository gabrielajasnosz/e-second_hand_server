package com.esecondhand.esecondhand.repository;

import com.esecondhand.esecondhand.domain.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Component
@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory,Integer> {

    List<MainCategory> findAllByDestinationSex(String sex);

}
