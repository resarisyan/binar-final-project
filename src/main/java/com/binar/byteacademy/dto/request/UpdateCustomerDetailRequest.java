package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerDetailRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String country;
    @NotBlank
    private String city;
}
