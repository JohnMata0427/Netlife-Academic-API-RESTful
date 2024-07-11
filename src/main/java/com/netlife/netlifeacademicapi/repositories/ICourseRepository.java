package com.netlife.netlifeacademicapi.repositories;

import com.netlife.netlifeacademicapi.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findById(String id);
}
