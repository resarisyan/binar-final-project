package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.PasswordReset;
import com.binar.byteacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {
    Optional<PasswordReset> findFirstByToken(String token);
    Optional<PasswordReset> findFirstByUser(User user);
}