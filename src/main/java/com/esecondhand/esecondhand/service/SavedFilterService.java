package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.SavedFilterDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterEntryDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterPreviewDto;
import com.esecondhand.esecondhand.domain.entity.SavedFilter;
import com.esecondhand.esecondhand.exception.ItemDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;

import java.util.List;

public interface SavedFilterService {
    SavedFilter saveFilters(SavedFilterEntryDto savedFilterEntryDto);

    List<SavedFilterPreviewDto> getSavedFilters();

    SavedFilterDto getSavedFilter(Long savedFilterId) throws ItemDoesntExistsException, ItemDoesntBelongToUserException;
}
