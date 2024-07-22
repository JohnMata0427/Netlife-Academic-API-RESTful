package com.netlife.netlifeacademicapi.helpers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSender {

    @Value("${url.frontend}")
    private String frontendUrl;

    @Autowired
    private JavaMailSender mailSender;

    public void verificationCodeEmail(String toUserMail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Código de verificación de Acceso a Netlife Academic");

            Path templatePath = Paths.get(getClass().getClassLoader().getResource("email-template.html").toURI());
            String htmlTemplate = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            String htmlMessage = htmlTemplate
            .replace("{{title}}", "Código de verificación de Acceso a Netlife Academic")
            .replace("{{verificationCode}}", verificationCode)
            .replace("{{name}}", "")
            .replace("{{first_message}}", "Bienvenido a NetLife Academic")
            .replace("{{second_message}}", "Para registrar tu cuenta, utiliza el siguiente código de verificación:")
            .replace("{{third_message}}", "Nos emociona tenerte en nuestra comunidad de aprendizaje.")
            .replace("{{fourth_message}}", "Ante cualquier duda, no dudes en contactar al soporte técnico.")
            .replace("{{button_text}}", "Registrate aquí")
            .replace("{{url}}", frontendUrl + "/auth/register");

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void recoveryPasswordEmail(String toUserMail, String user, String verificationCode, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Recuperación de contraseña de Netlife Academic");

            Path templatePath = Paths.get(getClass().getClassLoader().getResource("email-template.html").toURI());
            String htmlTemplate = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            String htmlMessage = htmlTemplate
            .replace("{{title}}", "Recibimos tu solicitud para restablecer contraseña")
            .replace("{{verificationCode}}", verificationCode)
            .replace("{{name}}", user)
            .replace("{{first_message}}", "Recibimos tu solicitud para restablecer tu contraseña.")
            .replace("{{second_message}}", "Para restablecer tu contraseña, utiliza el siguiente código de validación:")
            .replace("{{third_message}}", "Haz clic en el siguiente enlace para restablecer tu contraseña:")
            .replace("{{fourth_message}}", "Si no solicitaste restablecer tu contraseña, ignora este mensaje y sigue disfrutando de nuestros servicios.")
            .replace("{{button_text}}", "Restablecer contraseña")
            .replace("{{url}}", frontendUrl + "/auth/verify-code/" + token);

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
            helper.setSubject("Bienvenido a Netlife Academic");

            String htmlMessage = "<h1>Bienvenido a Netlife Academic</h1>"
                + "<p>Gracias por registrarte en Netlife Academic</p>"
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
            helper.setSubject("Cambio de contraseña de Netlife Academic");

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
            helper.setSubject("Cuenta bloqueada de Netlife Academic");

            String htmlMessage = "<h1>Cuenta Bloqueada</h1>"
                + "<p>Tu cuenta ha sido bloqueada, contacta a soporte para más información</p>";

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void profileUpdatedEmail(String toUserMail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Actualización de perfil de Netlife Academic");

            String htmlMessage = "<h1>Perfil Actualizado</h1>"
                + "<p>Tu perfil ha sido actualizado exitosamente</p>"
                + "<p>Si no fuiste tú, contacta a soporte</p>";

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo enviado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}