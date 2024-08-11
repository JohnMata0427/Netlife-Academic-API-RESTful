package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.services.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Object response = authService.registerUser(user);

        return ResponseEntity.status(response instanceof User ? 201: 400).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        Object loggedUser = authService.loginUser(body.get("email"), body.get("password"));
        return ResponseEntity.status(loggedUser instanceof ErrorResponse ? 401 : 200).body(loggedUser);
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestBody Map<String, String> body) {
        Object response = authService.recoveryPassword(body.get("email"));
        return ResponseEntity.status(response instanceof ErrorResponse ? 400 : 200).body(response);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyUserCode(@RequestParam String token, @RequestBody Map<String, String> body) {
        Object response = authService.verifyUserCode(body.get("verificationCode"), token);
        return ResponseEntity.status(response instanceof ErrorResponse ? 400 : 200).body(response);
    }

    @PostMapping("/new-password")
    public ResponseEntity<?> newUserPassword(@RequestParam String token, @RequestBody Map<String, String> changePassword) {
        Object response = authService.newUserPassword(changePassword.get("password"), changePassword.get("confirmPassword"), token);
        return ResponseEntity.status(response instanceof ErrorResponse ? 400 : 200).body(response);
    }
}
