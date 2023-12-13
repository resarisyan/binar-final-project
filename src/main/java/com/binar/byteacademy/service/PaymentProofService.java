package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.UpdatePaymentProofRequest;

public interface PaymentProofService {
    void updatePaymentProof(String slugCourse, UpdatePaymentProofRequest request);
}
