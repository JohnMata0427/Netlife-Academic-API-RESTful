package com.netlife.netlifeacademicapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
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

    private String teacher_id;

    @ManyToMany(mappedBy = "courses")
    private List<User> students;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
