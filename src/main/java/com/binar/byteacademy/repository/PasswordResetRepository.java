package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {
}