package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.dto.ItemListFiltersDto;
import com.esecondhand.esecondhand.domain.entity.Item;

import java.text.ParseException;
import java.util.List;

interface ItemRepositoryCustom {
    List<Item> findItems(ItemListFiltersDto itemListFiltersDto, List<Long> list, List<Long> followedIds) throws ParseException;
}
