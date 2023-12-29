package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.Material;
import com.binar.byteacademy.entity.MaterialActivity;
import com.binar.byteacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialActivityRepository extends JpaRepository<MaterialActivity, UUID> {
    Optional<MaterialActivity> findByUserAndMaterial(User user, Material material);
    Integer countByMaterial_Chapter_CourseAndUser(Course course, User user);
}
