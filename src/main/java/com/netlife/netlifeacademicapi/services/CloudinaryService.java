package com.netlife.netlifeacademicapi.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.netlife.netlifeacademicapi.models.Announcement;
import com.netlife.netlifeacademicapi.models.Course;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.IAnnouncementRepository;
import com.netlife.netlifeacademicapi.repositories.ICourseRepository;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAnnouncementRepository announcementRepository;

    @Autowired
    private ICourseRepository courseRepository;

    public Object uploadFileUser(MultipartFile multipartFile, String id) {

        User user;
        String imageUrl;

        try {
            user = userRepository.findById(id).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("Usuario no encontrado")
                    .status(404)
                    .error("Not Found")
                    .path("/users/" + id + "/upload-image")
                    .build();
        }

        try {
            imageUrl = cloudinary
                    .uploader()
                    .upload(multipartFile.getBytes(), Map.of("public_id", "user-" + user.getId()))
                    .get("url")
                    .toString();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("Error al subir la imagen")
                    .status(500)
                    .error("Internal Server Error")
                    .path("/users/" + id + "/upload-image")
                    .build();
        }

        user.setImageUrl(imageUrl);

        userRepository.save(user);

        return Map.of("message", "Imagen subida correctamente");
    }

    public Object uploadFileAnnouncement(MultipartFile multipartFile, String id) {

        Announcement announcement;
        String imageUrl;

        try {
            announcement = announcementRepository.findById(id).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("Anuncio no encontrado")
                    .status(404)
                    .error("Not Found")
                    .path("/announcements/" + id + "/upload-image")
                    .build();
        }

        try {
            imageUrl = cloudinary
                    .uploader()
                    .upload(multipartFile.getBytes(), Map.of("public_id", "announcement-" + announcement.getId()))
                    .get("url")
                    .toString();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("Error al subir la imagen")
                    .status(500)
                    .error("Internal Server Error")
                    .path("/announcements/" + id + "/upload-image")
                    .build();
        }

        announcement.setImageUrl(imageUrl);

        announcementRepository.save(announcement);

        return Map.of("message", "Imagen subida correctamente");
    }

    public Object uploadFileCourse(MultipartFile multipartFile, String id) {

        Course course;
        String imageUrl;

        try {
            course = courseRepository.findById(id).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("Curso no encontrado")
                    .status(404)
                    .error("Not Found")
                    .path("/courses/" + id + "/upload-image")
                    .build();
        }

        try {
            imageUrl = cloudinary
                    .uploader()
                    .upload(multipartFile.getBytes(), Map.of("public_id", "course-" + course.getId()))
                    .get("url")
                    .toString();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("Error al subir la imagen")
                    .status(500)
                    .error("Internal Server Error")
                    .path("/courses/" + id + "/upload-image")
                    .build();
        }

        course.setImageUrl(imageUrl);

        courseRepository.save(course);

        return Map.of("message", "Imagen subida correctamente");
    }
}
