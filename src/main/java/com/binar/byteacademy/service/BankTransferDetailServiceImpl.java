package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.dto.request.CreateBankTransferDetailRequest;
import com.binar.byteacademy.dto.request.UpdateBankTransferDetailRequest;
import com.binar.byteacademy.dto.response.BankTransferDetailResponse;
import com.binar.byteacademy.entity.BankTransferDetail;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.BankTransferDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.binar.byteacademy.common.util.Constants.TableName.BANK_TRANSFER_DETAIL_TABLE;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankTransferDetailServiceImpl implements BankTransferDetailService {
    private final BankTransferDetailRepository bankTransferDetailRepository;
    private final CheckDataUtil checkDataUtil;

    @Override
    public BankTransferDetailResponse addNewBankTransferDetail(CreateBankTransferDetailRequest request) {
        try {
            BankTransferDetail bankTransferDetail = BankTransferDetail.builder()
                    .bankName(request.getBankName().toLowerCase())
                    .accountNumber(request.getAccountNumber().toLowerCase())
                    .build();
            bankTransferDetail = bankTransferDetailRepository.save(bankTransferDetail);
            return BankTransferDetailResponse.builder()
                    .bankName(bankTransferDetail.getBankName())
                    .accountNumber(bankTransferDetail.getAccountNumber())
                    .build();
        }  catch (Exception e) {
            log.error("Failed to add new bank transfer detail");
            throw new ServiceBusinessException("Failed to add new bank transfer detail");
        }
    }

    @Override
    public void updateBankTransferDetail(String bankName, UpdateBankTransferDetailRequest request) {
        try {
            bankTransferDetailRepository.findFirstByBankName(bankName.toLowerCase())
                    .map(bankTransferDetail -> {
                        checkDataUtil.checkDataField(BANK_TRANSFER_DETAIL_TABLE, "bank_name", request.getBankName(), "bank_transfer_detail_id", bankTransferDetail.getId());
                        bankTransferDetail.setBankName(request.getBankName().toLowerCase());
                        bankTransferDetail.setAccountNumber(request.getAccountNumber().toLowerCase());
                        return bankTransferDetail;
                    })
                    .ifPresentOrElse(bankTransferDetailRepository::save, () -> {
                        throw new DataNotFoundException("Bank transfer detail not found");
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update bank transfer detail");
            throw new ServiceBusinessException("Failed to update bank transfer detail");
        }
    }

    @Override
    public void deleteBankTransferDetail(String bankName) {
        try {
            BankTransferDetail bankTransferDetail = bankTransferDetailRepository.findFirstByBankName(bankName.toLowerCase())
                    .orElseThrow(() -> new  DataNotFoundException("Bank transfer detail not found"));
            bankTransferDetailRepository.delete(bankTransferDetail);
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete bank transfer detail");
            throw new ServiceBusinessException("Failed to delete bank transfer detail");
        }
    }

    @Override
    public BankTransferDetailResponse getBankTransferDetail(String selectedBankName) {
        try {
            BankTransferDetail bankTransferDetail = bankTransferDetailRepository.findFirstByBankName(selectedBankName.toLowerCase())
                    .orElseThrow(() -> new  DataNotFoundException("Bank transfer detail not found"));
            return BankTransferDetailResponse.builder()
                    .bankName(bankTransferDetail.getBankName())
                    .accountNumber(bankTransferDetail.getAccountNumber())
                    .build();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get bank transfer detail");
            throw new ServiceBusinessException("Failed to get bank transfer detail");
        }
    }

    @Override
    public Page<BankTransferDetailResponse> getAllBankTransferDetail(Pageable pageable) {
        try {
            Page<BankTransferDetail> bankTransferDetailPage = Optional.of(bankTransferDetailRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException("Bank transfer detail not found"));
            return bankTransferDetailPage.map(bankTransferDetail -> BankTransferDetailResponse.builder()
                    .bankName(bankTransferDetail.getBankName())
                    .accountNumber(bankTransferDetail.getAccountNumber())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all bank transfer detail");
            throw new ServiceBusinessException("Failed to get all bank transfer detail");
        }
    }
}
