package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.exception.InvalidImagesNumberException;
import com.esecondhand.esecondhand.exception.InvalidItemPropertiesException;
import com.esecondhand.esecondhand.exception.ObjectDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ItemService {

    ItemDto saveItem(ItemEntryDto itemEntryDto) throws IOException, InvalidImagesNumberException, InvalidItemPropertiesException;

    FileSystemResource find(Long imageId);

    ItemDto getItem(Long itemId) throws ObjectDoesntExistsException;

    ItemDto editItem(EditItemDto editItemDto) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException;

    List<ItemPreviewDto> getItems(ItemListFiltersDto itemListFiltersDto) throws ParseException;

    void deleteItem(Long itemId) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException;

    PriceExtremeValuesDto getPriceExtremeValues();

    List<ItemPreviewDto> getHiddenItems();

    CountersDto getUserItemsCounters(Long userId) throws ObjectDoesntExistsException;

    void manageItemVisibility(Long itemId, boolean status) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException;

    List<ItemPreviewDto> getFollowedUsersItems(Long userId, int page, int pageSize);

    void reportItem(ReportDto reportDto);
}
