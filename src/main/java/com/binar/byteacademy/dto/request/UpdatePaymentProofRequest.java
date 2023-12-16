package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.validation.Base64Image;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentProofRequest {
    @NotBlank
    @Base64Image
    private String pathPaymentProofImage;
}
