package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.exception.ItemDontExistsException;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

public interface ItemService {

    Long saveItem(ItemEntryDto itemEntryDto) throws IOException;

    FileSystemResource find(Long imageId);

    ItemDto getItem(Long itemId) throws ItemDontExistsException;
}
