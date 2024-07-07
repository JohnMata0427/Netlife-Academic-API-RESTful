package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public Object createUser(@RequestBody User user) {
        return userService.createUser(user.getEmail(), user.getRole());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        Object updatedUser = userService.updateUser(id, user);
        return updatedUser instanceof User ? ResponseEntity.ok((User) updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserData(@PathVariable String id) {
        if (userService.deleteUserData(id)) {
            return ResponseEntity.ok().body("User deleted successfully");
        }
        return ResponseEntity.status(404).body(ErrorResponse.builder()
                .message("User not found")
                .status(404)
                .error("Not Found")
                .path("/api/users/" + id)
                .build());
    }

    @PutMapping("/lock-user")
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        Object response = userService.deleteUser(user.getEmail());
        return ResponseEntity.status(response instanceof User ? 200 : 404).body(response);
    }
}
