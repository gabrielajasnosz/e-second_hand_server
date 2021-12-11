package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.SizeDto;
import com.esecondhand.esecondhand.service.SizeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/sizes")
public class SizeController {

    private SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Map<Long, List<SizeDto>>> getSizes() {

        Map<Long, List<SizeDto>> sizes = sizeService.getSizes();

        return ResponseEntity.ok(sizes);

    }

    @RequestMapping(value = "/ungrouped", method = RequestMethod.GET)
    public ResponseEntity<List<SizeDto>> getSizesUngrouped() {

        List<SizeDto> sizes = sizeService.getSizesUngrouped();

        return ResponseEntity.ok(sizes);

    }
}
