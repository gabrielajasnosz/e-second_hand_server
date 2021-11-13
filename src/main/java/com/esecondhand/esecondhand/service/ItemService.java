package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.exception.ItemDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ItemService {

    ItemDto saveItem(ItemEntryDto itemEntryDto) throws IOException;

    FileSystemResource find(Long imageId);

    ItemDto getItem(Long itemId) throws ItemDoesntExistsException;

    ItemDto editItem(EditItemDto editItemDto) throws ItemDoesntExistsException, ItemDoesntBelongToUserException;

    List<ItemPreviewDto> getItems(ItemListFiltersDto itemListFiltersDto) throws ParseException;

    void deleteItem(Long itemId) throws ItemDoesntExistsException, ItemDoesntBelongToUserException;

    PriceExtremeValuesDto getPriceExtremeValues();

    void manageItemVisibility(Long itemId, boolean status) throws ItemDoesntExistsException, ItemDoesntBelongToUserException;
}
