package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.Item;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

public interface ItemService {

    Item saveItem(ItemEntryDto itemEntryDto) throws IOException;

    FileSystemResource find(Long imageId);
}
