package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminPurchaseResponse implements Serializable {
    private String courseName;
    private String tokenPurchase;
    private Integer amountPaid;
    private Double ppn;
    private LocalDateTime purchaseDate;
    private EnumPurchaseStatus purchaseStatus;
    private String username;
}
