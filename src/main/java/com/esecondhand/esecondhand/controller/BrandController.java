package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.service.BrandService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/brands")
public class BrandController {

    private BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<BrandDto>> getBrands() {

        List<BrandDto> brands = brandService.getBrands();

        return ResponseEntity.ok(brands);

    }
}
