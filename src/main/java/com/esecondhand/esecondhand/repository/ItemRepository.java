package com.esecondhand.esecondhand.repository;

import com.esecondhand.esecondhand.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {


}
