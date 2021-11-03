package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.exception.ItemDontExistsException;
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
    public ResponseEntity<Long> addItem(@Valid @ModelAttribute ItemEntryDto itemEntryDto) throws IOException {
        return ResponseEntity.ok(itemService.saveItem(itemEntryDto));

    }
    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    FileSystemResource downloadImage(@PathVariable Long imageId) throws Exception {
        return itemService.find(imageId);
    }

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public ResponseEntity<ItemDto> getItem(@RequestParam("itemId") Long itemId) {
        try {
            ItemDto itemDto = itemService.getItem(itemId);
            return ResponseEntity.ok(itemDto);
        } catch (ItemDontExistsException e) {
            return ResponseEntity.notFound().build();
        }

    }



}
