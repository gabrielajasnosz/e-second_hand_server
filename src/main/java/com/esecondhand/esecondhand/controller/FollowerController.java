package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.UserPreviewDto;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.FollowerService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/followers")
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @PostMapping("/{userId}")
    public ResponseEntity<?> addToFollowers(@PathVariable Long userId){
        try {
            followerService.addToFollowed(userId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteFromFollowed(@PathVariable Long userId) {
        followerService.deleteFromFollowed(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId){
        try {
            List<UserPreviewDto> followers = followerService.getFollowers(userId);
            return ResponseEntity.status(HttpStatus.OK).body(followers);
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/following/{userId}")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId){
        try {
            List<UserPreviewDto> followers = followerService.getFollowing(userId);
            return ResponseEntity.status(HttpStatus.OK).body(followers);
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
