package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.EmailVerification;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumEmailVerificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findFirstByTokenAndUserAndEmailVerificationType(String token, User user, EnumEmailVerificationType type);
    Optional<EmailVerification> findFirstByUserAndEmailVerificationType(User user, EnumEmailVerificationType type);
    Optional<EmailVerification> findFirstByTokenAndEmailVerificationType(String token, EnumEmailVerificationType type);

}
