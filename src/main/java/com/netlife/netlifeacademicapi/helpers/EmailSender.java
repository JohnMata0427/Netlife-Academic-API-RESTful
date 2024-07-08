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

    public void recoveryPasswordEmail(String toUserMail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Recuperación de contraseña de NetLife Academic");

            String htmlMessage = "<h1>Recuperación de contraseña</h1>"
                + "<p>Este es tu código de recuperación de contraseña: <strong>"
                + verificationCode + "</strong></p>"
                + "<br>"
                + "<p>Si no solicitaste la recuperación de contraseña, contacta a soporte</p>";

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void welcomeEmail(String toUserMail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Bienvenido a NetLife Academic");

            String htmlMessage = "<h1>Bienvenido a NetLife Academic</h1>"
                + "<p>Gracias por registrarte en NetLife Academic</p>"
                + "<p>Esperamos que disfrutes de nuestros servicios</p>";

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void changePasswordEmail(String toUserMail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Cambio de contraseña de NetLife Academic");

            String htmlMessage = "<h1>Cambio de contraseña</h1>"
                + "<p>Tu contraseña ha sido cambiada exitosamente</p>"
                + "<p>Si no fuiste tú, contacta a soporte</p>";

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void accountDeactivatedEmail(String toUserMail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Cuenta bloqueada de NetLife Academic");

            String htmlMessage = "<h1>Cuenta Bloqueada</h1>"
                + "<p>Tu cuenta ha sido bloqueada, contacta a soporte para más información</p>";

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}