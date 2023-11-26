package com.binar.byteacademy.entity;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id")
    private UUID id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_sub_title", nullable = false)
    private String courseSubTitle;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @Column(nullable = false)
    private Double price;

    @Column(name = "total_course_rate")
    private Double totalCourseRate;

    @Column(name = "total_modules")
    private Integer totalModules;

    @Column(name = "course_duration")
    private Integer courseDuration;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "course_description", columnDefinition = "text")
    private String courseDescription;

    @Column(name = "target_market", columnDefinition = "text")
    private String targetMarket;

    @Column(name = "slug_course")
    private String slugCourse;

    @Column(name = "path_course_image")
    private String pathCourseImage;

    @Column(name = "group_link", nullable = false)
    private String groupLink;

    @Column(name = "course_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumCourseType courseType;

    @Column(name = "course_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumCourseLevel courseLevel;

    @Column(name = "course_status")
    @Enumerated(EnumType.STRING)
    private EnumStatus courseStatus;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Chapter> chapters;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_promo",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "promo_id"))
    private List<Promo> promos;
}
