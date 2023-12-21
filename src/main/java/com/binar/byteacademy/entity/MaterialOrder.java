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
@Table(name = "material_orders")
public class MaterialOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "material_order_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prev_material_id", referencedColumnName = "material_id")
    private Material prevMaterial;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "next_material_id", referencedColumnName = "material_id")
    private Material nextMaterial;

    @Column(name = "serial_number", nullable = false)
    private Integer serialNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
