package com.esecondhand.esecondhand.rest;

import com.esecondhand.esecondhand.dto.ColorDto;
import com.esecondhand.esecondhand.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/colors")
public class ColorController {

    private CategoryService categoryService;

    public ColorController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<ColorDto>> getColors() {

        List<ColorDto> colors = categoryService.getColors();

        return ResponseEntity.ok(colors);

    }
}
