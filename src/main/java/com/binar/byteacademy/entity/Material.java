package com.binar.byteacademy.entity;

import com.binar.byteacademy.enumeration.EnumMaterialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
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

    @Column(name = "serial_number", nullable = false)
    private Integer serialNumber;

    @Column(name = "video_link", nullable = false)
    private String videoLink;

    @Column(name = "material_duration", nullable = false)
    private Integer materialDuration;

    @Column(name = "slug_material")
    private String slugMaterial;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false)
    private EnumMaterialType materialType;

    @ManyToOne
    @JoinColumn(name = "chapter_id", referencedColumnName = "chapter_id", nullable = false)
    private Chapter chapter;
}
