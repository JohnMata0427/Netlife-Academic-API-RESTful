package com.netlife.netlifeacademicapi.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void verificationCodeEmail(String toUserMail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Código de verificación de Acceso a NetLife Academic");

            String htmlMessage = "<h1>Código de verificación</h1>"
                + "<p>Este es tu código de verificación: <strong>"
                + verificationCode + "</strong></p>";

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}