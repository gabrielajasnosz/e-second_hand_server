package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.CommentDto;
import com.esecondhand.esecondhand.domain.dto.CommentEntryDto;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;

import java.util.List;

public interface CommentService {
    void addComment(CommentEntryDto commentEntryDto) throws ObjectDoesntExistsException;

    List<CommentDto> getComments(Long userId, int page);
}
