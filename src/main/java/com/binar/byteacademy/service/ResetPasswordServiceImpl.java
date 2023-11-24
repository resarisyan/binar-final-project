package com.binar.byteacademy.service;

import com.binar.byteacademy.entity.PasswordReset;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.PasswordResetRepository;
import com.binar.byteacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class ResetPasswordServiceImpl {
    private final JavaMailSender javaMailSender;
    private final PasswordResetRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    public void sendEmail(String email) {
        try {
            User user = userRepository.findFirstByEmail(email).orElseThrow(
                    () -> new DataNotFoundException("User not found")
            );
            String resetLink = generateResetToken(user);
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("Byte Academy");
            msg.setTo(user.getEmail());

            msg.setSubject("Reset Password");
            msg.setText("Hello \n\n" + "Please click on this link to Reset your Password :" + resetLink + ". \n\n"
                    + "Regards \n" + "Byte Academy");

            javaMailSender.send(msg);
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to send email");
        }
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String generateResetToken(User user) {
        String token = generateToken();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);
        PasswordReset resetToken = PasswordReset.builder()
                .token(token)
                .user(user)
                .expTime(expiryDateTime)
                .build();
        passwordResetTokenRepository.save(resetToken);
        return "http://localhost:8080/resetPassword/" + resetToken.getToken();
    }
}
