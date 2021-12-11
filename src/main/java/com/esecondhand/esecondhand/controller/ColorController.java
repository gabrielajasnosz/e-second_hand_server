package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.service.ColorService;
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

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ColorDto>> getColors() {

        List<ColorDto> colors = colorService.getColors();

        return ResponseEntity.ok(colors);

    }
}
