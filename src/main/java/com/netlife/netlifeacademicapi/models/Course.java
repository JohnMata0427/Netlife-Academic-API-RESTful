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

    private String category;

    private String imageUrl;

    private String description;

    private int duration;

    private String presentation;

    private String objectives, skills, attitudes;

    private List<String> modules;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private User teacher_id;

    @ManyToMany(mappedBy = "courses")
    private List<User> students;

    private Timestamp createdAt;

    private Timestamp finishAt;

    private Timestamp updatedAt;
}
