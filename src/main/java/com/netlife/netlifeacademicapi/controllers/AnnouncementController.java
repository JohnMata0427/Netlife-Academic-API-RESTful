package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.Announcement;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.services.AnnouncementService;
import com.netlife.netlifeacademicapi.services.CloudinaryService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public Object createUser(@RequestBody Announcement announcement) {
        return announcementService.createAnnouncement(announcement);
    }

    @GetMapping
    public List<Announcement> getAllUsers() {
        return announcementService.getAnnouncements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getUserById(@PathVariable String id) {
        Optional<Announcement> announcement = announcementService.getAnnouncementById(id);
        return announcement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile file, @PathVariable String id) {
        Object response = cloudinaryService.uploadFileAnnouncement(file, id);
        return ResponseEntity.status(response instanceof ErrorResponse ? 404 : 200).body(response);
    }
}
