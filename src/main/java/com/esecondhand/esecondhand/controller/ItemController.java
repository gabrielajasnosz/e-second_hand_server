package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.Item;
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
import java.text.ParseException;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    private final Integer DEFAULT_PAGE_SIZE = 20;

    private final String DEFAULT_SORTING_ORDER = "DESC";

    private final String DEFAULT_SORTING_COLUMN = "creationDate";


    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<ItemDto> addItem(@Valid @ModelAttribute ItemEntryDto itemEntryDto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.saveItem(itemEntryDto));

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

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<ItemListDto> getItems(@RequestBody ItemListFiltersDto itemListFiltersDto) throws ParseException {

        if(itemListFiltersDto.getPageSize() == null){
            itemListFiltersDto.setPageSize(DEFAULT_PAGE_SIZE);
        }
        if(itemListFiltersDto.getSortingColumn() == null){
            itemListFiltersDto.setSortingColumn(DEFAULT_SORTING_COLUMN);
        }
        if(itemListFiltersDto.getSortingOrder() == null){
            itemListFiltersDto.setSortingOrder(DEFAULT_SORTING_ORDER);
        }


        List<ItemPreviewDto> itemList = itemService.getItems(itemListFiltersDto);
        ItemListDto itemListDto = new ItemListDto();

        Object nextItemValue;
        Long nextItemIndex = null;
        if (itemList.size() > itemListFiltersDto.getPageSize()) {
            nextItemIndex = itemList.get(itemList.size() - 1).getId();
            if(itemListFiltersDto.getSortingColumn().equals("price")){
                nextItemValue = itemList.get(itemList.size() - 1).getPrice();
            }
            else {
                nextItemValue = itemList.get(itemList.size() - 1).getCreationDate().toString();
            }
            itemListDto.setNextItemId(nextItemIndex);
            itemListDto.setNextItemValue(nextItemValue);
            Long finalNextItemIndex = nextItemIndex;
            itemList.removeIf(item -> item.getId().equals(finalNextItemIndex));
        }

        itemListDto.setItemList(itemList);
        return ResponseEntity.ok().body(itemListDto);
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
