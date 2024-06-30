package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
    }

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<?> loggedUser = userService.loginUser(user.getEmail(), user.getPassword());
        return loggedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
