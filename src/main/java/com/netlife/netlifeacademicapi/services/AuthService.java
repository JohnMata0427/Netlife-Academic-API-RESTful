package com.netlife.netlifeacademicapi.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.netlife.netlifeacademicapi.helpers.EmailSender;
import com.netlife.netlifeacademicapi.helpers.UserBean;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final IUserRepository userRepository;

    @Autowired
    private final UserBean userBean;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final EmailSender emailSender;

    @Transactional
    public Object registerUser(User request) {

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

        if (user.isVerified()) {
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
        
        user.setIdentification(request.getIdentification());
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setPassword(userBean.passwordEncoder().encode(request.getPassword()));
        user.setVerificationCode(null);
        user.setVerified(true);
        user.setActive(true);

        userRepository.save(user);
        
        emailSender.welcomeEmail(user.getEmail(), user.getName());

        return Map.of("message", "Usuario registrado exitosamente", "token", jwtService.getToken(user.getId(), user.getRole()), "id", user.getId());
    }

    @Transactional
    public Object loginUser(String email, String password) {

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

        return Map.of("message", "Inicio de sesión exitoso", "token", jwtService.getToken(user.getId(), user.getRole()), "id", user.getId());
    }

    @Transactional
    public Object recoveryPassword(String email) {

        User user;

        try {
            user = userRepository.findByEmail(email).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("El correo " + email + " no se encuentra registrado")
                    .status(404)
                    .error("Not Found")
                    .path("/auth/recovery-password")
                    .build();
        }

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
        user.setRecoveryPassword(true);

        userRepository.save(user);

        System.out.println("Token: " + token);
        System.out.println("Verification Code: " + verificationCode);
        
        emailSender.recoveryPasswordEmail(email, user.getName(), verificationCode, token);

        return Map.of("message", "Correo enviado exitosamente");
    }

    public Object verifyUserCode(String verificationCode, String token) {

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

        if (!user.isRecoveryPassword()) {
            return ErrorResponse.builder()
                    .message("El usuario no cuenta con un código de verificación")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/verify-code?token=" + token)
                    .build();
        }

        if (!user.getVerificationCode().equals(verificationCode)){
            return ErrorResponse.builder()
                    .message("El código de verificación es incorrecto")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/verify-code?token=" + token)
                    .build();
        }

        user.setVerificationCode(null);

        userRepository.save(user);

        return Map.of("message", "El código de verificación es correcto");
    }

    public Object newUserPassword(String password, String confirmPassword, String token) {

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
                    .message("El usuario no cuenta con un código de verificación")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/new-password/" + token)
                    .build();
        }

        if (user.getVerificationCode() != null) {
            return ErrorResponse.builder()
                    .message("El usuario no ha verificado el código de verificación")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/new-password/" + token)
                    .build();
        }

        user.setToken(null);
        user.setRecoveryPassword(false);
        user.setPassword(userBean.passwordEncoder().encode(password));

        emailSender.changePasswordEmail(user.getEmail(), user.getName());

        userRepository.save(user);

        return Map.of("message", "Contraseña cambiada exitosamente");
    }

}