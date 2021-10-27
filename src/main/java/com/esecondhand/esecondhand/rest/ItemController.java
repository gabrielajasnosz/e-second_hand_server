package com.esecondhand.esecondhand.rest;
import com.esecondhand.esecondhand.domain.Item;
import com.esecondhand.esecondhand.dto.ItemEntryDto;
import com.esecondhand.esecondhand.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<Item> addItem(@Valid @ModelAttribute ItemEntryDto itemEntryDto) throws IOException {
        return ResponseEntity.ok(itemService.saveItem(itemEntryDto));

    }


}
