package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.SavedFilterDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterEntryDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterPreviewDto;
import com.esecondhand.esecondhand.domain.entity.SavedFilter;
import com.esecondhand.esecondhand.exception.ObjectDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;

import java.util.List;

public interface SavedFilterService {
    SavedFilter saveFilters(SavedFilterEntryDto savedFilterEntryDto);

    List<SavedFilterPreviewDto> getSavedFilters();

    void deleteFilter(Long id) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException;

    SavedFilterDto getSavedFilter(Long savedFilterId) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException;
}
