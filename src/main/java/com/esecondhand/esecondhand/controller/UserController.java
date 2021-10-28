package com.esecondhand.esecondhand.controller;


import com.esecondhand.esecondhand.domain.dto.JwtResponse;
import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import com.esecondhand.esecondhand.domain.dto.UserDto;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {

    private UserService userService;

    public UserController(@Lazy UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody UserDto userDto) throws Exception {

        final String token = userService.signIn(userDto);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 409, message = "Conflict")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody RegisterDto registerDto) {

        try {
            userService.save(registerDto);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }


}
