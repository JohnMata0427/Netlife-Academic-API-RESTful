package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.Course;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.MessageResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.services.CloudinaryService;
import com.netlife.netlifeacademicapi.services.CourseService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        Object response = courseService.createCourse(course);
        return ResponseEntity.status(response instanceof Course ? 200 : 404).body(response);
    }
}
