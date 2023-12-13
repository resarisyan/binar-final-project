package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ChangePasswordRequest;
import com.binar.byteacademy.dto.request.LoginRequest;
import com.binar.byteacademy.dto.request.RegisterRequest;
import com.binar.byteacademy.dto.response.LoginResponse;
import com.binar.byteacademy.dto.response.RefreshTokenResponse;
import com.binar.byteacademy.dto.response.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
