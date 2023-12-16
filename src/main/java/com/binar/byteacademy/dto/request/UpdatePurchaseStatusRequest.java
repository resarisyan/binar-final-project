package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePurchaseStatusRequest {
    @NotNull
    private EnumPurchaseStatus purchaseStatus;
}
