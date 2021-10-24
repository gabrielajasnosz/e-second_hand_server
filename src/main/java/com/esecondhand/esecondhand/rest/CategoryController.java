package com.esecondhand.esecondhand.rest;

import com.esecondhand.esecondhand.domain.Category;
import com.esecondhand.esecondhand.dto.*;
import com.esecondhand.esecondhand.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> getCategories() {

        return ResponseEntity.ok(categoryService.getCategories());

    }

    @RequestMapping(value = "/brands", method = RequestMethod.GET)
    public ResponseEntity<List<BrandDto>> getBrands() {

        List<BrandDto> brands = categoryService.getBrands();

        return ResponseEntity.ok(brands);

    }

    @RequestMapping(value = "/sizes", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<SizeDto>>> getSizes() {

        Map<String, List<SizeDto>> sizes = categoryService.getSizes();

        return ResponseEntity.ok(sizes);

    }



}
