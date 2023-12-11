package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.BankTransferDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankTransferDetailRepository extends JpaRepository<BankTransferDetail, UUID> {
    Optional<BankTransferDetail> findFirstByBankName(String bankName);
}
