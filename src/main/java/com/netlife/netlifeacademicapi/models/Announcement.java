package com.netlife.netlifeacademicapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement {

    @Id
    private String id;

    private String subject;
    private String content;
    private String imageUrl;
    private String type;
    private String role;
    private boolean guest;
    private String state;
    private boolean sendEmail;
    private boolean publishHome;
    private List<String> excludedEmails;
    private Timestamp createdAt;
    private Timestamp deletedAt;
}
