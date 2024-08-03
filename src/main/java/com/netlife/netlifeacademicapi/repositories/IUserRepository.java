package com.netlife.netlifeacademicapi.repositories;

import com.netlife.netlifeacademicapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByToken(String token);

    @Query("SELECT u.email FROM User u WHERE u.role = ?1 AND u.verified = true AND u.email NOT IN ?2")
    List<String> findEmails(String role, List<String> excludedEmails);

    @Query("SELECT u.email FROM User u WHERE u.verified = true AND u.deleted = false")
    List<String> findAllEmails();

    boolean existsByEmail(String email);

}
