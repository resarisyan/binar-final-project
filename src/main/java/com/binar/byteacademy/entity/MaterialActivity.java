package com.binar.byteacademy.entity;

import com.binar.byteacademy.entity.compositekey.MaterialActivityKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "material_activities")
public class MaterialActivity {
    @EmbeddedId
    private MaterialActivityKey id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("materialId")
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", nullable = false)
    private Material material;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
