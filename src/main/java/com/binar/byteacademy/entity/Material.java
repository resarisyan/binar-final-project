package com.binar.byteacademy.entity;

import com.binar.byteacademy.enumeration.EnumMaterialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "material_id")
    private UUID id;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "serial_number", nullable = false)
    private Integer serialNumber;

    @Column(name = "video_link", nullable = false)
    private String videoLink;

    @Column(name = "material_duration", nullable = false)
    private Integer materialDuration;

    @Column(name = "slug_material", unique = true)
    private String slugMaterial;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false)
    private EnumMaterialType materialType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapter_id", referencedColumnName = "chapter_id", nullable = false)
    private Chapter chapter;

    @OneToMany(mappedBy = "material", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MaterialActivity> materialActivities;

    @OneToMany(mappedBy = "material", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MaterialOrder> materialOrders;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
