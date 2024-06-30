package com.netlife.netlifeacademicapi.services;

import com.netlife.netlifeacademicapi.helpers.UserBean;
import com.netlife.netlifeacademicapi.models.Role;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User registerUser(User request) {

        User user = User.builder()
                        .id(UUID.randomUUID().toString())
                        .name(request.getName())
                        .lastname(request.getLastname())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .password(userBean.passwordEncoder().encode(request.getPassword()))
                        .role(Role.STUDENT)
                        .token(jwtService.getToken(request))
                        .confirmEmail(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .updatedAt(new Timestamp(System.currentTimeMillis()))
                        .build();
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> updateUser(String id, User user) {
        if (!userRepository.existsById(id)) return Optional.empty();
        User lastUser = userRepository.findById(id).get();
        user.setId(id);
        user.setName(user.getName() == null ? lastUser.getName() : user.getName());
        user.setLastname(user.getLastname() == null ? lastUser.getLastname() : user.getLastname());
        user.setEmail(user.getEmail() == null ? lastUser.getEmail() : user.getEmail());
        user.setPhone(user.getPhone() == null ? lastUser.getPhone() : user.getPhone());
        user.setPassword(user.getPassword() == null ? lastUser.getPassword() : userBean.passwordEncoder().encode(user.getPassword()));
        user.setRole(user.getRole() == null ? lastUser.getRole() : user.getRole());
        user.setToken(lastUser.getToken());
        user.setConfirmEmail(lastUser.isConfirmEmail());
        user.setCreatedAt(lastUser.getCreatedAt());
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return Optional.of(userRepository.save(user));
    }

    @Transactional
    public boolean deleteUser(String id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Optional<?> loginUser(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetails user = userRepository.findByEmail(email).orElseThrow();
        String token = jwtService.getToken(user);
        return Optional.of(token);
    }
}
