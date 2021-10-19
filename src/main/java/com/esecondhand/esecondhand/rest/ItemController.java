package com.esecondhand.esecondhand.rest;
import com.esecondhand.esecondhand.dto.ItemDto;
import com.esecondhand.esecondhand.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


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
