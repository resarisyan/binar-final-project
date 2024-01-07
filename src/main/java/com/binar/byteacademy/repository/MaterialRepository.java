package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Chapter;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
    Optional<Material> findBySlugMaterial(String slugMaterial);
    Optional<Material> findFirstByChapterAndSerialNumberGreaterThan(Chapter chapter, Integer serialNumber);
    Optional<Material> findFirstByChapterAndSerialNumberLessThan(Chapter chapter, Integer serialNumber);
    Integer countByChapter_Course(Course course);
    boolean existsBySlugMaterial(String slugMaterial);
}
