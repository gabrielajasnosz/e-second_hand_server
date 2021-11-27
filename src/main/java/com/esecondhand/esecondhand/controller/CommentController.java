package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.CommentDto;
import com.esecondhand.esecondhand.domain.dto.CommentEntryDto;
import com.esecondhand.esecondhand.exception.ObjectDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/comments")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentEntryDto commentEntryDto) throws ObjectDoesntExistsException {
        try {
            commentService.addComment(commentEntryDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();

        }
    }
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<List<CommentDto>> getComments(@RequestParam("userId") Long userId, @RequestParam("page") int page) throws ObjectDoesntExistsException {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(userId, page));
    }
}
