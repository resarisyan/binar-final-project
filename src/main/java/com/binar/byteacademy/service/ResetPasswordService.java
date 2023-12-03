package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ForgotPasswordRequest;
import com.binar.byteacademy.dto.request.ResetPasswordRequest;

public interface ResetPasswordService {
    void sendEmail(ForgotPasswordRequest request);
    void resetPassword(String token, ResetPasswordRequest request);
}
