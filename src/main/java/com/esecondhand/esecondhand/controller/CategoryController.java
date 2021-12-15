package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.domain.dto.CategoryDto;
import com.esecondhand.esecondhand.domain.dto.SizeDto;
import com.esecondhand.esecondhand.service.CategoryService;
import com.esecondhand.esecondhand.service.serviceImpl.CategoryServiceImpl;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> getCategories() {

        return ResponseEntity.ok(categoryService.getCategories());

    }

}
