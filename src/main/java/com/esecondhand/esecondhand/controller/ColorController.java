package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.service.ColorService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/colors")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }


    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ColorDto>> getColors() {

        List<ColorDto> colors = colorService.getColors();

        return ResponseEntity.ok(colors);

    }
}
