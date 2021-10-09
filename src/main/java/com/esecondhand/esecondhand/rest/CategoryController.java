package com.esecondhand.esecondhand.rest;
import com.esecondhand.esecondhand.dto.MainCategoryDto;
import com.esecondhand.esecondhand.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @RequestMapping(value = "/womenCategories", method = RequestMethod.GET)
    public ResponseEntity<List<MainCategoryDto>> getWomenCategories() throws Exception {

        List<MainCategoryDto> womenCategories = categoryService.getWomenCategories();

        return ResponseEntity.ok(womenCategories);

    }

}
