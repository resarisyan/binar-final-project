package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateOtpRegisterPhoneRequest {
    @NotBlank
    @Size(min = 10, max = 14)
    private String phoneNumber;
}
