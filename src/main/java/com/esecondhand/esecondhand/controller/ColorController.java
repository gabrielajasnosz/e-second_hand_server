package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.service.CategoryService;
import com.esecondhand.esecondhand.service.serviceImpl.CategoryServiceImpl;
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

    private final CategoryService categoryService;

    public ColorController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<ColorDto>> getColors() {

        List<ColorDto> colors = categoryService.getColors();

        return ResponseEntity.ok(colors);

    }
}
