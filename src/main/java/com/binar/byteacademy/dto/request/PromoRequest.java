package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoRequest {
    @NotBlank
    private String promoName;
    @NotBlank
    private String promoDescription;
    @NotBlank
    private String promoCode;
    @NotNull
    @Digits(integer = 3, fraction = 2)
    private Double discount;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
}
