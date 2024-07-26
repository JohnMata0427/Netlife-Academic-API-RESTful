package com.netlife.netlifeacademicapi.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private IUserRepository userRepository;

    public Object uploadFile(MultipartFile multipartFile, String id) {

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
    
}
