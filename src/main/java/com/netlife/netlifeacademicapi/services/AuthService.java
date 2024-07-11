package com.netlife.netlifeacademicapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.netlife.netlifeacademicapi.helpers.EmailSender;
import com.netlife.netlifeacademicapi.helpers.UserBean;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.MessageResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserBean userBean;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailSender emailSender;

    @Transactional
    public Object registerUser(User request) {

        if (request.getName() == null || request.getLastname() == null || request.getPassword() == null || request.getEmail() == null)
            return ErrorResponse.builder()
                    .message("Todos los campos son requeridos")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();

        User user;

        try {
            user = userRepository.findByEmail(request.getEmail()).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("El correo " + request.getEmail() + " no se encuentra registrado")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();
        }

        if (user.isDeleted()) {
            return ErrorResponse.builder()
                    .message("La cuenta se encuentra bloqueada")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();
        }

        if (user.isVerified() || user.getVerificationCode() == null) {
            return ErrorResponse.builder()
                    .message("El usuario ya se encuentra verificado")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();
        }

        if (!user.getVerificationCode().equals(request.getVerificationCode()))
            return ErrorResponse.builder()
                    .message("Código de verificación incorrecto")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();
        
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setPassword(userBean.passwordEncoder().encode(request.getPassword()));
        user.setVerificationCode(null);
        user.setVerified(true);
        user.setActive(true);

        emailSender.welcomeEmail(user.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    public Object loginUser(String email, String password) {
        if (password == null || email == null) {
            return ErrorResponse.builder()
                    .message("Todos los campos son requeridos")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/login")
                    .build();
        }
        User user;

        try {
            user = userRepository.findByEmail(email).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("El correo " + email + " no se encuentra registrado")
                    .status(404)
                    .error("Not Found")
                    .path("/auth/login")
                    .build();
        }

        if (!userBean.passwordEncoder().matches(password, user.getPassword())) {
            return ErrorResponse.builder()
                    .message("Contraseña incorrecta")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/login")
                    .build();
        }
        
        if (user.isDeleted()) {
            return ErrorResponse.builder()
                    .message("La cuenta se encuentra bloqueada")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();
        }

        // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        String token = jwtService.getToken(user.getId(), user.getRole());

        return MessageResponse.builder()
                .message("Inicio de sesión exitoso")
                .token(token)
                .build();
    }

    @Transactional
    public Object recoveryPassword(String email) {

        if (email == null) {
            return ErrorResponse.builder()
                    .message("El correo es requerido")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/recovery-password")
                    .build();
        }

        if (!userRepository.existsByEmail(email)) {
            return ErrorResponse.builder()
                    .message("El correo " + email + " no se encuentra registrado")
                    .status(404)
                    .error("Not Found")
                    .path("/auth/recovery-password")
                    .build();
        }

        User user = userRepository.findByEmail(email).get();

        if (user.isDeleted()) {
            return ErrorResponse.builder()
                    .message("La cuenta se encuentra bloqueada")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/recovery-password")
                    .build();
        }

        String token = userBean.generateToken();
        String verificationCode = ((Math.random() * 99999) + 100000 + "").substring(0, 6);

        user.setToken(token);
        user.setVerificationCode(verificationCode);

        emailSender.recoveryPasswordEmail(email, verificationCode, token);

        userRepository.save(user);

        return MessageResponse.builder()
                .message("Código de verificación enviado al correo " + email)
                .token(token)
                .build();
    }

    public Object verifyUserCode(String verificationCode, String token) {
        if (verificationCode == null) {
            return ErrorResponse.builder()
                    .message("Todos los campos son requeridos")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/verify-code?token=" + token)
                    .build();
        }

        User user;

        try {
            user = userRepository.findByToken(token).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("El token no es válido")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/verify-code?token=" + token)
                    .build();
        }

        if (!user.getVerificationCode().equals(verificationCode)) {
            return ErrorResponse.builder()
                    .message("Código de verificación incorrecto")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/verify-code?token=" + token)
                    .build();
        }

        user.setVerificationCode(null);
        user.setRecoveryPassword(true);

        userRepository.save(user);

        return MessageResponse.builder()
                .message("Usuario verificado exitosamente")
                .build();
    }

    public Object newUserPassword(String password, String confirmPassword, String token) {
        if (password == null || confirmPassword == null) {
            return ErrorResponse.builder()
                    .message("Todos los campos son requeridos")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/new-password/" + token)
                    .build();
        }

        User user;

        try {
            user = userRepository.findByToken(token).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("El token no es válido")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/new-password/" + token)
                    .build();
        }

        if (!user.isRecoveryPassword()) {
            return ErrorResponse.builder()
                    .message("El usuario no ha ingresado el código de verificación")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/new-password/" + token)
                    .build();
        }

        if (!password.equals(confirmPassword)) {
            return ErrorResponse.builder()
                    .message("Las contraseñas no coinciden")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/new-password/" + token)
                    .build();
        }

        if (userBean.passwordEncoder().matches(password, user.getPassword())) {
            return ErrorResponse.builder()
                    .message("La contraseña no puede ser igual a la anterior")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/new-password/" + token)
                    .build();
        }

        user.setToken(null);
        user.setRecoveryPassword(false);
        user.setPassword(userBean.passwordEncoder().encode(password));

        userRepository.save(user);

        emailSender.changePasswordEmail(user.getEmail());

        return MessageResponse.builder()
                .message("Contraseña actualizada exitosamente")
                .build();
    }

}