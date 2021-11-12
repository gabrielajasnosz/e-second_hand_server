package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.dto.ItemListFiltersDto;
import com.esecondhand.esecondhand.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    Optional<Item> findById(Long itemId);
}
