package com.binar.byteacademy.entity;

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
@Table(name = "promo")
public class Promo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "promo_id")
    private UUID id;

    @Column(name = "promo_code", unique = true)
    private String promoCode;

    @Column(nullable = false)
    private Double discount;

    @Column(columnDefinition = "text")
    private String promoDescription;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
}
