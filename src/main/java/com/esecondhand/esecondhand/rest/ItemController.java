package com.esecondhand.esecondhand.rest;

import com.esecondhand.esecondhand.domain.ItemDto;
import com.esecondhand.esecondhand.dto.MainCategoryDto;
import com.esecondhand.esecondhand.service.CategoryService;
import com.esecondhand.esecondhand.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ResponseEntity<?> addItem(ItemDto itemDto) {
        return (ResponseEntity<?>) ResponseEntity.ok();

    }


}
