package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ResetPasswordRequest;
import com.binar.byteacademy.enumeration.EnumEmailVerificationType;

public interface EmailVerificationService {
    void sendEmail(String email, EnumEmailVerificationType type);
    void verifyEmailTokenRegister(String token);
    void verifyChangeEmailToken(String token);
    void verifyEmailTokenForgotPassword(ResetPasswordRequest request);
}
