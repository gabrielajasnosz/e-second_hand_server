package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.service.ItemService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<Item> addItem(@Valid @ModelAttribute ItemEntryDto itemEntryDto) throws IOException {
        return ResponseEntity.ok(itemService.saveItem(itemEntryDto));

    }
    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    FileSystemResource downloadImage(@PathVariable Long imageId) throws Exception {
        return itemService.find(imageId);
    }



}
