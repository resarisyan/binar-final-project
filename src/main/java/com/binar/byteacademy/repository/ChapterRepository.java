package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Chapter;
import com.binar.byteacademy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, UUID> {
    boolean existsBySlugChapter(String slugChapter);
    Optional<Chapter> findFirstBySlugChapter(String slugChapter);
}
