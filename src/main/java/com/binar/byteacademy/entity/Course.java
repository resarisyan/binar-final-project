package com.binar.byteacademy.entity;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.enumeration.EnumCourseType;
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
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id")
    private UUID id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "total_course_rate")
    private Double totalCourseRate;

    @Column(name = "total_chapter", nullable = false)
    private Integer totalChapter;

    @Column(name = "course_duration", nullable = false)
    private Integer courseDuration;

    @Column(name = "course_description", columnDefinition = "text")
    private String courseDescription;

    @Column(name = "target_market", columnDefinition = "text")
    private String targetMarket;

    @Column(name = "slug_course", unique = true, nullable = false)
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

    @Column(name = "course_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumStatus courseStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<UserProgress> userProgresses;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<CoursePromo> coursePromos;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<CourseRating> courseRatings;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Discussion> discussions;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<UserProgress> userProgress;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}