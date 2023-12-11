package com.binar.byteacademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_proof")
public class PaymentProof {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_proof_id")
    private UUID id;

    @Column(name = "path_payment_proof_image", nullable = false)
    private String pathPaymentProofImage;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;
}
