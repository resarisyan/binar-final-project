package com.binar.byteacademy.dto.request.purchase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardPurchaseRequest {
    @NotBlank
    private String slugCourse;

    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Card Number should be a 16-digit number")
    private String cardNumber;

    @NotBlank
    private String cardHolderName;

    @NotBlank
    @Pattern(regexp = "\\d{3}", message = "CVV should be a 3-digit number")
    private String cvv;

    @NotNull
    @Pattern(regexp = "^(0[1-9]|1[0-2])/(\\d{2})$", message = "Expiry date should be in MM/yy format")
    private String expiryDate;
}