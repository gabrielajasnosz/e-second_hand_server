package com.esecondhand.esecondhand.rest;


import com.esecondhand.esecondhand.dto.JwtResponse;
import com.esecondhand.esecondhand.dto.UserDto;
import com.esecondhand.esecondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody UserDto user) throws Exception {
        return ResponseEntity.ok(userService.save(user));
    }


}
