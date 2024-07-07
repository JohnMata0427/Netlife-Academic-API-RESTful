package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Object response = authService.registerUser(user);

        return ResponseEntity.status(response instanceof User ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Object loggedUser = authService.loginUser(user.getEmail(), user.getPassword());
        return ResponseEntity.status(loggedUser instanceof User ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(loggedUser);
    }
}
