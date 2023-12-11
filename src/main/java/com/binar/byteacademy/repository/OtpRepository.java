package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Otp;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumOtpType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepository extends JpaRepository<Otp, UUID> {
    Optional<Otp> findFirstByOtpCodeAndUserAndOtpType(String otpCode, User user, EnumOtpType otpType);
    Optional<Otp> findFirstByUserAndOtpType(User user, EnumOtpType otpType);
}
