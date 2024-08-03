package com.netlife.netlifeacademicapi.services;

import com.netlife.netlifeacademicapi.helpers.AnnouncementEmailSender;
import com.netlife.netlifeacademicapi.models.Announcement;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.repositories.IAnnouncementRepository;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    @Autowired
    private final IAnnouncementRepository announcementRepository;

    @Autowired
    private final AnnouncementEmailSender announcementEmailSender;

    @Autowired
    private final IUserRepository userRepository;

    @Transactional
    public List<Announcement> getAnnouncements() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementById(String id) {
        return announcementRepository.findById(id);
    }

    public Object createAnnouncement(Announcement announcement) {
        if (announcement.getSubject().isEmpty() || announcement.getContent().isEmpty() || announcement.getType().isEmpty()
                || announcement.getRole().isEmpty() || announcement.getState().isEmpty()) {
            return ErrorResponse.builder().message("Todos los campos son requeridos").status(400).error("Bad Request")
                    .path("/api/announcements").build();
        }

        if (!announcement.isSendEmail() && !announcement.isPublishHome()) {
            return ErrorResponse.builder().message("Debe seleccionar al menos una opción de notificación").status(400)
                    .error("Bad Request").path("/api/announcements").build();
        }

        if (announcement.isPublishHome() && announcement.getDeletedAt() == null) {
            return ErrorResponse.builder()
                    .message("Debe seleccionar una fecha de eliminación para la publicación en la página de Inicio")
                    .status(400).error("Bad Request").path("/api/announcements").build();
        }

        if (announcement.isPublishHome() && announcement.getImageUrl().isEmpty()) {
            return ErrorResponse.builder()
                    .message("Debe seleccionar una imagen para la publicación en la página de Inicio").status(400)
                    .error("Bad Request").path("/api/announcements").build();
        }

        if (announcement.isSendEmail()) {
            String[] userEmails;

            // userEmails = (announcement.getRole() == "ALL"
            //         ? userRepository.findAllEmails()
            //         : userRepository.findEmails(announcement.getRole(), announcement.getExcludedEmails())).toArray(new String[0]);

            userEmails = userRepository.findAllEmails().toArray(new String[0]);

            announcementEmailSender.announcementEmail(userEmails, announcement.getSubject(), announcement.getContent(),
                    announcement.getRole());
        }

        announcement.setId(UUID.randomUUID().toString());

        return announcementRepository.save(announcement);
    }
}