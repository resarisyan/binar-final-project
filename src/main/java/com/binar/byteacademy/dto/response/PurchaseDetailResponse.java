package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.dto.response.PaymentProofResponse;
import com.binar.byteacademy.enumeration.EnumPaymentMethod;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseDetailResponse {
    private Double amountPaid;
    private Double ppn;
    private LocalDateTime endPaymentDate;
    private EnumPurchaseStatus purchaseStatus;
    private EnumPaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private CourseResponse course;
    private PaymentProofResponse paymentProofResponse;
}
