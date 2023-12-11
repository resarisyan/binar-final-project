package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateBankTransferDetailRequest;
import com.binar.byteacademy.dto.request.UpdateBankTransferDetailRequest;
import com.binar.byteacademy.dto.response.BankTransferDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankTransferDetailService {
    BankTransferDetailResponse addNewBankTransferDetail(CreateBankTransferDetailRequest request);
    void updateBankTransferDetail(String bankName, UpdateBankTransferDetailRequest request);
    void deleteBankTransferDetail(String bankName);
    BankTransferDetailResponse getBankTransferDetail(String selectedBankName);
    Page<BankTransferDetailResponse> getAllBankTransferDetail(Pageable pageable);
}
