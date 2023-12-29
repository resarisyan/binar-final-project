package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, UUID> {
    Optional<Chapter> findFirstBySlugChapter(String slugChapter);
    boolean existsBySlugChapter(String slugChapter);
    Optional<List<Chapter>> findAllByCourse_SlugCourse(String slugCourse);
}
