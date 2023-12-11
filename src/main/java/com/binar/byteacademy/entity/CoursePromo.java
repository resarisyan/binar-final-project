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
@Table(name="course_promos")
public class CoursePromo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_promo_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "promo_id", referencedColumnName = "promo_id")
    private Promo promo;
}
