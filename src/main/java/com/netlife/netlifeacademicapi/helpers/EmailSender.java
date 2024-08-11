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

    private final Path templatePath;
    
    private EmailSender() throws Exception {
        this.templatePath = Paths.get(getClass().getClassLoader().getResource("email-template.html").toURI());
    }

    public void verificationCodeEmail(String toUserMail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Código de verificación de Acceso a Netlife Academic");

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

            System.out.println("Correo de verificación enviado exitosamente a " + toUserMail);
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

            System.out.println("Correo de recuperación de contraseña enviado exitosamente a " + toUserMail);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void welcomeEmail(String toUserMail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Bienvenido a Netlife Academic");

            String htmlTemplate = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            String htmlMessage = htmlTemplate
            .replace("{{title}}", "Bienvenido a Netlife Academic")
            .replace("{{verificationCode}}", "")
            .replace("{{name}}", username)
            .replace("{{first_message}}", "Tu cuenta ha sido creada exitosamente.")
            .replace("{{second_message}}", "Nos emociona tenerte en nuestra comunidad de aprendizaje.")
            .replace("{{third_message}}", "Ante cualquier duda, no dudes en contactar al soporte técnico.")
            .replace("{{fourth_message}}", "Para comenzar a disfrutar de nuestros servicios, puedes ir al inicio aquí:")
            .replace("{{button_text}}", "Ir al inicio de Netlife Academic")
            .replace("{{url}}", frontendUrl + "/home");


            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo de bienvenida enviado exitosamente a " + toUserMail);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void changePasswordEmail(String toUserMail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Cambio de contraseña de Netlife Academic");

            String htmlTemplate = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            String htmlMessage = htmlTemplate
            .replace("{{title}}", "Cambio de contraseña de Netlife Academic")
            .replace("{{verificationCode}}", "")
            .replace("{{name}}", username)
            .replace("{{first_message}}", "Tu contraseña ha sido cambiada exitosamente.")
            .replace("{{second_message}}", "Tu información personal es segura con nosotros.")
            .replace("{{third_message}}", "Si no realizaste estos cambios, contacta a soporte técnico.")
            .replace("{{fourth_message}}", "Para comenzar a disfrutar de nuestros servicios, inicia sesión:")
            .replace("{{button_text}}", "Iniciar sesión")
            .replace("{{url}}", frontendUrl + "/auth/login");

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo de cambio de contraseña enviado exitosamente a " + toUserMail);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void accountDeactivatedEmail(String toUserMail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Cuenta bloqueada de Netlife Academic");

            String htmlTemplate = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            String htmlMessage = htmlTemplate
            .replace("{{title}}", "Cuenta bloqueada de Netlife Academic")
            .replace("{{verificationCode}}", "")
            .replace("{{name}}", username)
            .replace("{{first_message}}", "Lamentamos informarte que tu cuenta ha sido bloqueada.")
            .replace("{{second_message}}", "Ha sido un placer tenerte en nuestra comunidad de aprendizaje.")
            .replace("{{third_message}}", "Si crees que esto es un error, contacta a soporte técnico.")
            .replace("{{fourth_message}}", "Haz clic en el siguiente enlace para contactar a soporte:")
            .replace("{{button_text}}", "Contactar soporte")
            .replace("{{url}}", frontendUrl + "/contact");

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo de cuenta bloqueada enviado exitosamente a " + toUserMail);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void profileUpdatedEmail(String toUserMail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Actualización de perfil de Netlife Academic");

            String htmlTemplate = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            String htmlMessage = htmlTemplate
            .replace("{{title}}", "Actualización de perfil de Netlife Academic")
            .replace("{{verificationCode}}", "")
            .replace("{{name}}", username)
            .replace("{{first_message}}", "Tu perfil ha sido actualizado exitosamente.")
            .replace("{{second_message}}", "Tu información personal es segura con nosotros.")
            .replace("{{third_message}}", "Si no realizaste estos cambios, contacta a soporte técnico.")
            .replace("{{fourth_message}}", "Puedes ver tus cambios en tu perfil:")
            .replace("{{button_text}}", "Ver perfil")
            .replace("{{url}}", frontendUrl + "/mi-perfil");

            helper.setText(htmlMessage, true);

            mailSender.send(message);

            System.out.println("Correo de actualización de perfil enviado exitosamente a " + toUserMail);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void announcementEmail(String[] toUserMails, String subject, String content, String type, String role, boolean guest, String state) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMails);
            helper.setSubject(subject);

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