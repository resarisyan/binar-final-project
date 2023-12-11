package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.CoursePromo;
import com.binar.byteacademy.entity.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CoursePromoRepository extends JpaRepository<CoursePromo, UUID> {
    Optional<CoursePromo> findFirstByCourseAndPromo(Course course, Promo promo);
}
