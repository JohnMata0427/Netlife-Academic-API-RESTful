package com.netlife.netlifeacademicapi.controllers;

import com.netlife.netlifeacademicapi.models.Course;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.services.CloudinaryService;
import com.netlife.netlifeacademicapi.services.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile file, @PathVariable String id) {
        Object response = cloudinaryService.uploadFileCourse(file, id);
        return ResponseEntity.status(response instanceof ErrorResponse ? 404 : 200).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        Object response = courseService.getCourses();
        return ResponseEntity.status(response instanceof ErrorResponse ? 404 : 200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable String id) {
        Object response = courseService.getCourse(id);
        return ResponseEntity.status(response instanceof ErrorResponse ? 404 : 200).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody Course course) {
        Object response = courseService.updateCourse(id, course);
        return ResponseEntity.status(response instanceof ErrorResponse ? 404 : 200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        Object response = courseService.deleteCourse(id);
        return ResponseEntity.status(response instanceof ErrorResponse ? 404 : 200).body(response);
    }
}
