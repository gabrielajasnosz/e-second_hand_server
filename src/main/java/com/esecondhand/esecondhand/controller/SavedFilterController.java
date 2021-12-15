package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.SavedFilterDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterEntryDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterPreviewDto;
import com.esecondhand.esecondhand.domain.entity.SavedFilter;
import com.esecondhand.esecondhand.exception.ObjectDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.SavedFilterService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SavedFilter> saveFilters(@RequestBody SavedFilterEntryDto savedFilterEntryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFilterService.saveFilters(savedFilterEntryDto));

    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFilter(@RequestParam("id") Long id){
        try {
            savedFilterService.deleteFilter(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ObjectDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<SavedFilterPreviewDto>> getFiltersList() {
        return ResponseEntity.status(HttpStatus.OK).body(savedFilterService.getSavedFilters());
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SavedFilterDto> getFilter(@RequestParam("id") Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(savedFilterService.getSavedFilter(id));
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ObjectDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
