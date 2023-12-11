package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyRegisterPhoneRequest {
    @NotBlank
    @Size(min = 10, max = 14)
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 6)
    @Digits(integer = 6, fraction = 0)
    private String otp;
}
