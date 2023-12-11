package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.PaymentProof;
import com.binar.byteacademy.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentProofRepository extends JpaRepository<PaymentProof, UUID> {
    Optional<PaymentProof> findFirstByPurchase(Purchase purchase);
}
