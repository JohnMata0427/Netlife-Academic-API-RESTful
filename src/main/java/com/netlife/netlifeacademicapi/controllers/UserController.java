package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.services.CloudinaryService;
import com.netlife.netlifeacademicapi.services.UserService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

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

    @GetMapping("/ranking")
    public ResponseEntity<?> getRankedUsers() {
        Object response = userService.getRankedUsers();
        return ResponseEntity.status(response instanceof List ? 200 : 404).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        Object updatedUser = userService.updateUser(id, user);
        return updatedUser instanceof User ? ResponseEntity.ok((User) updatedUser) : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile file, @PathVariable String id) {
        Object response = cloudinaryService.uploadFile(file, id);
        return ResponseEntity.status(response instanceof ErrorResponse ? 404 : 200).body(response);
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
