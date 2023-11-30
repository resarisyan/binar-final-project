package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.MailUtil;
import com.binar.byteacademy.dto.request.ForgotPasswordRequest;
import com.binar.byteacademy.dto.request.ResetPasswordRequest;
import com.binar.byteacademy.entity.PasswordReset;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ForbiddenException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.PasswordResetRepository;
import com.binar.byteacademy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ResetPasswordServiceImpl implements ResetPasswordService {
    private final MailUtil mailUtil;
    private final PasswordResetRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void sendEmail(ForgotPasswordRequest request) {
        try {
            User user = userRepository.findFirstByEmail(request.getEmail()).filter(
                    User::isVerified
            ).orElseThrow(
                    () -> new DataNotFoundException("User not found or not verified")
            );
            String resetLink = generateResetToken(user);
            mailUtil.sendEmailResetAsync(user.getEmail(), resetLink);
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) {
        try {
            PasswordReset passwordResetToken = passwordResetTokenRepository.findFirstByToken(token).orElseThrow(
                    () -> new DataNotFoundException("Token not found")
            );
            User user = passwordResetToken.getUser();
            user.setPassword(request.getPassword());
            userRepository.save(user);
            passwordResetTokenRepository.delete(passwordResetToken);
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String generateResetToken(User user) {
        try {
            String token = generateToken();
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);
            Optional<PasswordReset> passwordResetToken = passwordResetTokenRepository.findFirstByUser(user);
            passwordResetToken.ifPresent(passwordReset -> {
                if (passwordReset.getExpTime().isAfter(currentDateTime.plusMinutes(10))) {
                    throw new ForbiddenException("Reset token still valid");
                } else {
                    passwordResetTokenRepository.delete(passwordReset);
                }
            });
            PasswordReset resetToken = PasswordReset.builder()
                    .token(token)
                    .user(user)
                    .expTime(expiryDateTime)
                    .build();
            passwordResetTokenRepository.save(resetToken);
            return "http://localhost:8080/resetPassword/" + resetToken.getToken();
        } catch (ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to generate reset token");
        }
    }
}
