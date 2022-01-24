package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.dto.ItemListFiltersDto;
import com.esecondhand.esecondhand.domain.entity.Item;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.List;

@Repository
interface ItemRepositoryCustom {
    List<Item> findItems(ItemListFiltersDto itemListFiltersDto, List<Long> list, List<Long> followedIds) throws ParseException;
}
