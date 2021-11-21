package com.esecondhand.esecondhand.controller;


import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;
import com.esecondhand.esecondhand.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    private UserService userService;


    private final ApplicationEventPublisher applicationEventPublisher;

    public UserController(@Lazy UserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody LoginDto loginDto) throws Exception {

        String token = userService.signIn(loginDto);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 409, message = "Conflict")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody RegisterDto registerDto, HttpServletRequest request) {

        try {
            User registered = userService.save(registerDto);

            String appUrl = request.getContextPath();
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl));

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/confirmRegistration", method = RequestMethod.GET)
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {


        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.NOT_FOUND);
        }

        User user = verificationToken.getUser();

        if (user.isEnabled()) {
            return new ResponseEntity<>("Your account is already enabled", HttpStatus.BAD_REQUEST);
        }
        Calendar cal = Calendar.getInstance();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            String messageValue = "Token has expired.";
            System.out.println(messageValue);
            return new ResponseEntity<>("Token expired", HttpStatus.BAD_REQUEST);
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return new ResponseEntity<>("Account confirmed! You can now sign in to your account", HttpStatus.OK);
    }

    @RequestMapping(value = "/search-for-users", method = RequestMethod.GET)
    public ResponseEntity<List<UserPreviewDto>> findUsers(@RequestParam("name") String name) {
        List<UserPreviewDto> users = userService.findUsers(name);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @RequestMapping(value = "/get-user", method = RequestMethod.GET)
    public ResponseEntity<UserDto> findUser(@RequestParam("id") Long id) throws ItemDoesntExistsException {
        UserDto user = userService.findUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
