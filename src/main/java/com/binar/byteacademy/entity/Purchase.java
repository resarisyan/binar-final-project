package com.binar.byteacademy.entity;

import com.binar.byteacademy.enumeration.EnumPaymentMethod;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "purchase_id")
    private UUID id;

    @Column(name = "path_proof_payment")
    private String pathProofPayment;

    @Column(name = "ppn")
    private Double ppn;

    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "end_payment_time", nullable = false)
    private LocalDateTime endPaymentDate;

    @Column(name = "purchase_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPurchaseStatus purchaseStatus;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPaymentMethod paymentMethod;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
    private Course course;
}
