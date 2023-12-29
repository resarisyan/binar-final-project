package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.AdminPurchaseResponse;
import com.binar.byteacademy.dto.response.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.Map;

public interface PurchaseService {
    PurchaseResponse createPurchase(String courseSlug, Principal connectedUser);
    void paymentCallback(Map<String, Object> request);
    Page<PurchaseResponse> getAllPurchaseDetailsForCustomer(Pageable pageable, Principal connectedUser);
    Page<AdminPurchaseResponse> getAllPurchaseDetailsForAdmin(Pageable pageable);
}
