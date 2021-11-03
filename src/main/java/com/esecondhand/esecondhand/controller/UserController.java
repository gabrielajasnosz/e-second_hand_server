package com.esecondhand.esecondhand.controller;


import com.esecondhand.esecondhand.domain.dto.JwtResponse;
import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import com.esecondhand.esecondhand.domain.dto.UserDto;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

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
    public ResponseEntity<?> signIn(@RequestBody UserDto userDto) throws Exception {

        String token = userService.signIn(userDto);

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
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = "Token has expired.";
            System.out.println(messageValue);
            return new ResponseEntity<>("Token expired", HttpStatus.BAD_REQUEST);
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return new ResponseEntity<>("Account confirmed! You can now sign in to your account", HttpStatus.OK);
    }

}
