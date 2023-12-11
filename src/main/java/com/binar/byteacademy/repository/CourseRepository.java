package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Course;

import com.binar.byteacademy.enumeration.EnumStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Optional<Course> findFirstBySlugCourse(String slugCourse);
    Page<Course> findAll(Specification<Course> specification, Pageable pageable);
    Optional<Course> findFirstBySlugCourseAndCourseStatus(String slugCourse, EnumStatus courseStatus);
}
