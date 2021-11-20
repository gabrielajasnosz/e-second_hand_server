package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.SavedFilterDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterEntryDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterPreviewDto;
import com.esecondhand.esecondhand.domain.entity.SavedFilter;
import com.esecondhand.esecondhand.exception.ItemDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;
import com.esecondhand.esecondhand.service.SavedFilterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/filters")
public class SavedFilterController {

    private final SavedFilterService savedFilterService;

    public SavedFilterController(SavedFilterService savedFilterService) {
        this.savedFilterService = savedFilterService;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<SavedFilter> saveFilters(@RequestBody SavedFilterEntryDto savedFilterEntryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFilterService.saveFilters(savedFilterEntryDto));

    }

    @RequestMapping(value = "/get-saved-filters", method = RequestMethod.GET)
    public ResponseEntity<List<SavedFilterPreviewDto>> saveFiltersList() {
        return ResponseEntity.status(HttpStatus.OK).body(savedFilterService.getSavedFilters());
    }

    @RequestMapping(value = "/get-filter", method = RequestMethod.GET)
    public ResponseEntity<SavedFilterDto> getFilter(@RequestParam("id") Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(savedFilterService.getSavedFilter(id));
        } catch (ItemDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ItemDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
