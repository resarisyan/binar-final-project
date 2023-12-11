package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.UpdatePurchaseStatusRequest;
import com.binar.byteacademy.dto.request.purchase.BankTransferPurchaseRequest;
import com.binar.byteacademy.dto.request.purchase.CreditCardPurchaseRequest;
import com.binar.byteacademy.dto.response.AdminPurchaseDetailResponse;
import com.binar.byteacademy.dto.response.PurchaseDetailResponse;
import com.binar.byteacademy.dto.response.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PurchaseService {
    PurchaseResponse makeBankTransferPurchase(BankTransferPurchaseRequest request);
    PurchaseResponse makeCreditCardPurchase(CreditCardPurchaseRequest request);
    Page<PurchaseDetailResponse> getAllPurchaseDetailsForCustomer(Pageable pageable);
    Page<AdminPurchaseDetailResponse> getAllPurchaseDetailsForAdmin(Pageable pageable);

    void updatePurchaseStatus(UpdatePurchaseStatusRequest request);
}
