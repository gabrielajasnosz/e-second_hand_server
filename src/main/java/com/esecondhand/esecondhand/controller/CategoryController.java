package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.domain.dto.CategoryDto;
import com.esecondhand.esecondhand.domain.dto.SizeDto;
import com.esecondhand.esecondhand.service.CategoryService;
import com.esecondhand.esecondhand.service.serviceImpl.CategoryServiceImpl;
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

    private final CategoryService categoryService;

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
    public ResponseEntity<Map<Long, List<SizeDto>>> getSizes() {

        Map<Long, List<SizeDto>> sizes = categoryService.getSizes();

        return ResponseEntity.ok(sizes);

    }



}
