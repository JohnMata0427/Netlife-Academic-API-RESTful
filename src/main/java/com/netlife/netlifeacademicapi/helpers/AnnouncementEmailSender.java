package com.netlife.netlifeacademicapi.helpers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class AnnouncementEmailSender {

    @Value("${url.frontend}")
    private String frontendUrl;

    @Autowired
    private JavaMailSender mailSender;

    private final Path templatePath;
    
    private AnnouncementEmailSender() throws Exception {
        this.templatePath = Paths.get(getClass().getClassLoader().getResource("announcement-email-template.html").toURI());
    }

    public void announcementEmail(String[] toUserMails, String subject, String content, String role) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMails);
            helper.setSubject(subject);

            if (role.equals("ALL")) role = "Usuarios";
            else if (role.equals("STUDENT")) role = "Estudiantes";
            else if (role.equals("TEACHER")) role = "Docentes";

            String htmlTemplate = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            String htmlMessage = htmlTemplate
            .replace("{{title}}", subject)
            .replace("{{role}}" , role)
            .replace("{{message}}", content);

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo de anuncio enviado exitosamente a " + toUserMails.length + " usuarios");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}