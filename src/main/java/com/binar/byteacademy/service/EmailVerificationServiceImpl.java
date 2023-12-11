package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.common.util.MailUtil;
import com.binar.byteacademy.dto.request.ResetPasswordRequest;
import com.binar.byteacademy.entity.EmailVerification;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumEmailVerificationType;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ForbiddenException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.exception.ValidationException;
import com.binar.byteacademy.repository.EmailVerificationRepository;
import com.binar.byteacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final MailUtil mailUtil;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void sendEmail(String email, EnumEmailVerificationType type) {
        try {
            String finalEmail;
            if (EnumEmailVerificationType.CHANGE_EMAIL.equals(type)) {
                finalEmail = jwtUtil.getUser().getEmail();
            } else {
                finalEmail = email;
            }

            userRepository.findFirstByEmail(finalEmail).filter(
                    user -> {
                        if (EnumEmailVerificationType.REGISTER.equals(type)) {
                            return !user.isVerifiedEmail();
                        } else {
                            return user.isVerifiedEmail();
                        }
                    }
            ).ifPresentOrElse(user -> {
                String verifyEmailLink;
                String subject;
                String text;

                if (EnumEmailVerificationType.REGISTER.equals(type)) {
                    verifyEmailLink = generateEmailToken(null, user, "http://localhost:8080/api/v1/email/verify/register/", type);
                    subject = "Verify your email";
                    text = "Please verify your email by clicking this link: " + verifyEmailLink;
                    mailUtil.sendEmailVerificationAsync(finalEmail, subject, text);
                } else if (EnumEmailVerificationType.FORGOT_PASSWORD.equals(type)) {
                    verifyEmailLink = generateEmailToken(null, user, "http://localhost:8080/api/v1/email/verify/forgot-password/", type);
                    subject = "Reset your password";
                    text = "Please reset your password by clicking this link: " + verifyEmailLink;
                    mailUtil.sendEmailVerificationAsync(finalEmail, subject, text);
                } else {
                    verifyEmailLink = generateEmailToken(email, user, "http://localhost:8080/api/v1/email/verify/change-email/", type);
                    subject = "Change your email";
                    text = "Please change your email by clicking this link: " + verifyEmailLink;
                    mailUtil.sendEmailVerificationAsync(email, subject, text);
                }
            }, () -> {
                if (EnumEmailVerificationType.REGISTER.equals(type)) {
                    throw new ForbiddenException("User not found or already verified");
                } else {
                    throw new DataNotFoundException("User not found or not verified");
                }
            });
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public void verifyEmailTokenRegister(String token) {
        try {
            emailVerificationRepository.findFirstByTokenAndEmailVerificationType(token, EnumEmailVerificationType.REGISTER).ifPresentOrElse(emailVerification -> {
                if (emailVerification.getExpTime().isBefore(LocalDateTime.now())) {
                    throw new ForbiddenException("Token expired");
                }
                User user = emailVerification.getUser();
                user.setVerifiedEmail(true);
                userRepository.save(user);
                emailVerification.setUser(null);
                emailVerificationRepository.delete(emailVerification);
            }, () -> {
                throw new DataNotFoundException("Token not found");
            });
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public void verifyEmailTokenForgotPassword(ResetPasswordRequest request) {
        try {
            emailVerificationRepository.findFirstByTokenAndEmailVerificationType(request.getToken(), EnumEmailVerificationType.FORGOT_PASSWORD).ifPresentOrElse(emailVerification -> {
                if (emailVerification.getExpTime().isBefore(LocalDateTime.now())) {
                    throw new ForbiddenException("Token expired");
                }
                if (!request.getPassword().equals(request.getConfirmPassword())) {
                        throw new ValidationException("Password and confirm password not match");
                }
                User user = emailVerification.getUser();
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(user);
                emailVerification.setUser(null);
                emailVerificationRepository.delete(emailVerification);
            }, () -> {
                throw new DataNotFoundException("Token not found");
            });
        } catch (DataNotFoundException | ForbiddenException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to reset password");
        }
    }

    @Override
    public void verifyChangeEmailToken(String token) {
        try {
            User user = jwtUtil.getUser();
            emailVerificationRepository.findFirstByTokenAndUserAndEmailVerificationType(token, user, EnumEmailVerificationType.CHANGE_EMAIL).ifPresentOrElse(emailVerification -> {
                if (emailVerification.getExpTime().isBefore(LocalDateTime.now())) {
                    throw new ForbiddenException("Token expired");
                }
                user.setEmail(emailVerification.getEmail());
                userRepository.save(user);
                emailVerification.setUser(null);
                emailVerificationRepository.delete(emailVerification);
            }, () -> {
                throw new DataNotFoundException("Token not found");
            });
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private String generateEmailToken(String email, User user, String url, EnumEmailVerificationType type) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            emailVerificationRepository.findFirstByUserAndEmailVerificationType(user, type).ifPresent(emailVerification -> {
                if (emailVerification.getExpTime().isAfter(currentDateTime)) {
                    throw new ForbiddenException("Please wait for 10 minutes to generate new token");
                } else {
                    emailVerificationRepository.delete(emailVerification);
                }
            });
            String token = generateToken();
            EmailVerification emailToken = EmailVerification.builder()
                    .token(token)
                    .email(email)
                    .emailVerificationType(type)
                    .user(user)
                    .expTime(currentDateTime.plusMinutes(10)).build();
            emailVerificationRepository.save(emailToken);
            return url + emailToken.getToken();
        } catch (ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to generate email token");
        }
    }
}
