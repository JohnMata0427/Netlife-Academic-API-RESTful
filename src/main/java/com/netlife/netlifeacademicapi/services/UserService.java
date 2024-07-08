package com.netlife.netlifeacademicapi.services;

import com.netlife.netlifeacademicapi.helpers.EmailSender;
import com.netlife.netlifeacademicapi.helpers.UserBean;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.MessageResponse;
import com.netlife.netlifeacademicapi.models.Role;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserBean userBean;

    @Autowired
    EmailSender emailSender;

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Object createUser(String email, Role role) {
        String verificationCode = ((Math.random() * 99999) + 100000 + "").substring(0, 6);

        if (userRepository.existsByEmail(email)) {
            return ErrorResponse.builder()
                    .message("El correo " + email + " ya se encuentra registrado")
                    .status(400)
                    .error("Bad Request")
                    .path("/users")
                    .build();
        }

        if (email == null) {
            return ErrorResponse.builder()
                    .message("El correo es requerido")
                    .status(400)
                    .error("Bad Request")
                    .path("/users")
                    .build();
        }

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .role(role)
                .password(userBean.passwordEncoder().encode(UUID.randomUUID().toString().substring(0, 8)))
                .verificationCode(verificationCode)
                .verified(false)
                .recoveryPassword(false)
                .deleted(false)
                .active(false)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        emailSender.verificationCodeEmail(email, verificationCode);

        return userRepository.save(user);
    }

    @Transactional
    public Object updateUser(String id, User request) {
        if (!userRepository.existsById(id)) {
            return ErrorResponse.builder()
                    .message("El usuario con id " + id + " no se encuentra registrado")
                    .status(404)
                    .error("Not Found")
                    .path("/users/" + id)
                    .build();
        }

        User user = userRepository.findById(id).get();

        if (request.getName() != null) user.setName(request.getName());
        if (request.getLastname() != null) user.setLastname(request.getLastname());
        if (request.getPassword() != null) user.setPassword(userBean.passwordEncoder().encode(request.getPassword()));
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getCompany() != null) user.setCompany(request.getCompany());
        if (request.getArea() != null) user.setArea(request.getArea());
        if (request.getPosition() != null) user.setPosition(request.getPosition());
        if (request.getBirthdate() != null) user.setBirthdate(request.getBirthdate());
        if (request.getImage() != null) user.setImage(request.getImage());
        if (request.isDeleted()) user.setDeleted(!request.isDeleted());
        
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return userRepository.save(user);
    }

    @Transactional
    public boolean deleteUserData(String id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Object deleteUser(String email) {
        if (!userRepository.existsByEmail(email)) {
            return ErrorResponse.builder()
                    .message("El usuario con correo " + email + " no se encuentra registrado")
                    .status(404)
                    .error("Not Found")
                    .path("/users/lock-user")
                    .build();
        }

        if (email == null) {
            return ErrorResponse.builder()
                    .message("El correo es requerido")
                    .status(400)
                    .error("Bad Request")
                    .path("/users/lock-user")
                    .build();
        }

        User user = userRepository.findByEmail(email).get();
        user.setDeleted(true);
        user.setActive(false);
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        emailSender.accountDeactivatedEmail(email);

        return MessageResponse.builder()
                .message("El usuario con correo " + email + " ha sido bloqueado")
                .build();
    }
}
