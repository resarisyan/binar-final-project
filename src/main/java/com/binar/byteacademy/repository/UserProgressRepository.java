package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.entity.UserProgress;
import com.binar.byteacademy.entity.compositekey.UserProgressKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, UserProgressKey> {
    Optional<UserProgress> findByCourse_SlugCourseAndUserAndIsCompleted(String slugCourse, User user, Boolean isCompleted);
    Optional<UserProgress> findByUserAndCourse(User user, Course course);
}
