package com.netlife.netlifeacademicapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    private String id;

    private String name;

    private String description;

    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "courses_teachers",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<String> teacher_id;

    @ManyToMany
    @JoinTable(
        name = "courses_students",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<User> students;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
