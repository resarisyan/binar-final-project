package com.binar.byteacademy.entity;

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

    @Column(name = "ppn")
    private Double ppn;

    @Column(name = "amount_paid", nullable = false)
    private Integer amountPaid;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "purchase_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPurchaseStatus purchaseStatus;

    @Column(name = "token_purchase")
    private String tokenPurchase;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
