package com.netlife.netlifeacademicapi.repositories;

import com.netlife.netlifeacademicapi.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseRepository extends JpaRepository<Course, String> {
}
