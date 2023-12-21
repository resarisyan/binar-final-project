package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Discussion;
import com.binar.byteacademy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DiscussionRepository extends JpaRepository<Discussion, UUID> {
    Optional<Discussion> findByDiscussionTopic(String discussionTopic);
    Optional<Discussion> findBySlugDiscussion(String slugDiscussion);
    Optional<Discussion> findBySlugDiscussionAndUser(String slugDiscussion, User user);
    Page<Discussion> findAllByCourse_SlugCourse(Pageable pageable, String slugCourse);
}