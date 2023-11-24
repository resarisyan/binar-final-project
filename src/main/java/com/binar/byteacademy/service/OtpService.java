package com.binar.byteacademy.service;


public interface OtpService {
    void verifyAccount(String otpCode, String username);
    void generateOtp(String username, String phoneNumber);
}
