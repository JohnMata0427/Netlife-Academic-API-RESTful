package com.netlife.netlifeacademicapi.services;

import com.netlife.netlifeacademicapi.helpers.EmailSender;
import com.netlife.netlifeacademicapi.helpers.UserBean;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.Role;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
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
    private EmailSender emailSender;

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Object getRankedUsers() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "points"));

        return userRepository.findAll(pageable).getContent();
    }

    @Transactional
    public Object findAllEmails() {
        return userRepository.findAllEmails();
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

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .imageUrl("https://res.cloudinary.com/ddcs9xxid/image/upload/v1720924124/profile_icbsmd.png")
                .name("Usuario")
                .lastname("Nuevo")
                .company("Netlife")
                .level("Colocar nivel")
                .area("Colocar área")
                .position("Colocar cargo")
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

        if (request.getName() != null)
            user.setName(request.getName());
        if (request.getLastname() != null)
            user.setLastname(request.getLastname());
        if (request.getCompany() != null)
            user.setCompany(request.getCompany());
        if (request.getLevel() != null)
            user.setLevel(request.getLevel());
        if (request.getArea() != null)
            user.setArea(request.getArea());
        if (request.getPosition() != null)
            user.setPosition(request.getPosition());
        if (request.getBirthdate() != null)
            user.setBirthdate(request.getBirthdate());
        if (request.getState() != null)
            user.setState(request.getState());
        if (request.isDeleted())
            user.setDeleted(!request.isDeleted());

        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        emailSender.profileUpdatedEmail(user.getEmail(), user.getName());

        return userRepository.save(user);
    }

    @Transactional
    public boolean deleteUserData(String id) {
        if (!userRepository.existsById(id))
            return false;
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

        User user = userRepository.findByEmail(email).get();
        user.setDeleted(true);
        user.setActive(false);
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        emailSender.accountDeactivatedEmail(email, user.getName());
        userRepository.save(user);

        return Map.of("message", "El usuario " + user.getEmail() + " ha sido eliminado correctamente");
    }
}
