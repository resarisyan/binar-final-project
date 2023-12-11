package com.binar.byteacademy.dto.request.purchase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardPurchaseRequest {
    @NotBlank
    private String slugCourse;

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String cardHolderName;

    @NotBlank
    private String cvv;

    @NotNull
    private LocalDate expiryDate;
}