package com.netlife.netlifeacademicapi.repositories;

import com.netlife.netlifeacademicapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
    boolean existsByEmail(String email);
}
