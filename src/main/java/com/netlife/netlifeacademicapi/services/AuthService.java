package com.netlife.netlifeacademicapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.netlife.netlifeacademicapi.helpers.UserBean;
import com.netlife.netlifeacademicapi.models.AuthResponse;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
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

        if (!user.getVerificationCode().equals(request.getVerificationCode()))
            return ErrorResponse.builder()
                    .message("Código de verificación incorrecto")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();
        
        if (user.isVerified())
            return ErrorResponse.builder()
                    .message("El usuario ya se encuentra verificado")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();

        if (request.getName() == null || request.getLastname() == null || request.getPassword() == null)
            return ErrorResponse.builder()
                    .message("Todos los campos son requeridos")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/register")
                    .build();

        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setPassword(userBean.passwordEncoder().encode(request.getPassword()));
        user.setVerificationCode(null);
        user.setVerified(true);
        user.setToken(jwtService.getToken(user));

        return userRepository.save(user);
    }

    @Transactional
    public Object loginUser(String email, String password) {
        if (!userRepository.existsByEmail(email)) {
            return ErrorResponse.builder()
                    .message("El correo " + email + " no se encuentra registrado")
                    .status(404)
                    .error("Not Found")
                    .path("/auth/login")
                    .build();
        }

        if (password == null || email == null) {
            return ErrorResponse.builder()
                    .message("Todos los campos son requeridos")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/login")
                    .build();
        }

        UserDetails user = userRepository.findByEmail(email).get();

        if (!userBean.passwordEncoder().matches(password, user.getPassword())) {
            return ErrorResponse.builder()
                    .message("Contraseña incorrecta")
                    .status(400)
                    .error("Bad Request")
                    .path("/auth/login")
                    .build();
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
