package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.EditItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.exception.ItemDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;
import com.esecondhand.esecondhand.service.ItemService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
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
        } catch (ItemDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public ResponseEntity<ItemDto> editItem(@Valid @RequestBody EditItemDto editItemDto) {
        try {
            ItemDto itemDto = itemService.editItem(editItemDto);
            System.out.println(itemDto);
            return ResponseEntity.status(HttpStatus.OK).body(itemDto);
        } catch (ItemDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ItemDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteItem(@RequestParam("itemId") Long itemId) {
        try {
            itemService.deleteItem(itemId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ItemDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ItemDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @RequestMapping(value = "/itemVisibility", method = RequestMethod.PUT)
    public ResponseEntity<?> changeItemVisibility(@RequestParam("itemId") Long itemId, @RequestParam("status") boolean status) {
        try {
            itemService.manageItemVisibility(itemId, status);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ItemDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ItemDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

}
