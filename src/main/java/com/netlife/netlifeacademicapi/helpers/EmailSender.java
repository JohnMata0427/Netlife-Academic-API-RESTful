package com.netlife.netlifeacademicapi.helpers;

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

    public void recoveryPasswordEmail(String toUserMail, String verificationCode, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Recuperación de contraseña de NetLife Academic");

            String htmlMessage = "<h1>Recuperación de contraseña</h1>"
                + "<p>Este es tu código de recuperación de contraseña: <strong>"
                + verificationCode + "</strong></p>"
                + "<br>"
                + "<button style=\"background-color: #4CAF50;\n"
                + "border: none;\n"
                + "color: white;\n"
                + "padding: 15px 32px;\n"
                + "text-align: center;\n"
                + "text-decoration: none;\n"
                + "display: inline-block;\n"
                + "font-size: 16px;\n"
                + "margin: 4px 2px;\n"
                + "cursor: pointer;\n"
                + "border-radius: 10px;\n"
                + "box-shadow: 0 9px #999999;\">\n"
                + "<a href=\"" + frontendUrl + "/auth/verify-code?token=" + token + "\">Recuperar contraseña</a>\n"
                + "</button>"
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

    public void profileUpdatedEmail(String toUserMail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(toUserMail);
            helper.setSubject("Actualización de perfil de NetLife Academic");

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