package com.esecondhand.esecondhand.rest;
import com.esecondhand.esecondhand.dto.MainCategoryDto;
import com.esecondhand.esecondhand.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, Map<String,List<MainCategoryDto>>>> getCategories(){

        Map<String, Map<String,List<MainCategoryDto>>>  categories = categoryService.getCategories();

        return ResponseEntity.ok(categories);

    }


}
