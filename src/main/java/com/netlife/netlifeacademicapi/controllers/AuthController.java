package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.ChangePassword;
import com.netlife.netlifeacademicapi.models.MessageResponse;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Object response = authService.registerUser(user);

        return ResponseEntity.status(response instanceof User ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Object loggedUser = authService.loginUser(user.getEmail(), user.getPassword());
        return ResponseEntity.status(loggedUser instanceof MessageResponse ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(loggedUser);
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestBody User user) {
        Object response = authService.recoveryPassword(user.getEmail());
        return ResponseEntity.status(response instanceof MessageResponse ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/verify-code/{token}")
    public ResponseEntity<?> verifyUserCode(@PathVariable String token, @RequestBody User user) {
        Object response = authService.verifyUserCode(user.getVerificationCode(), token);
        return ResponseEntity.status(response instanceof MessageResponse ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/new-password/{token}")
    public ResponseEntity<?> newUserPassword(@PathVariable String token, @RequestBody ChangePassword changePassword) {
        Object response = authService.newUserPassword(changePassword.getPassword(), changePassword.getConfirmPassword(), token);
        return ResponseEntity.status(response instanceof MessageResponse ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }
}
