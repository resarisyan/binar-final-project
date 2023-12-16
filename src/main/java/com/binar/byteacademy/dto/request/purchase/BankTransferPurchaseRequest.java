package com.binar.byteacademy.dto.request.purchase;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTransferPurchaseRequest {
    @NotBlank
    private String slugCourse;
}
