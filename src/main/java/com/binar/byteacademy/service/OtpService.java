package com.binar.byteacademy.service;


import com.binar.byteacademy.dto.request.OtpRequest;
import com.binar.byteacademy.dto.request.VerifyRegisterPhoneRequest;
import com.binar.byteacademy.enumeration.EnumOtpType;

public interface OtpService {
    void verifyRegisterPhoneNumber(VerifyRegisterPhoneRequest request);
    void verifyChangePhoneNumber(OtpRequest request);
    void sendOtp(String phoneNumber, EnumOtpType otpType);
}
