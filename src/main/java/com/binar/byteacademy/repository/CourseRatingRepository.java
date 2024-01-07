package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.CourseRating;
import com.binar.byteacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, UUID> {
    Optional<CourseRating> findByCourse_SlugCourseAndUser(String slugCourse, User user);
}
