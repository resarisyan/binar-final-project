package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DiscussionRepository extends JpaRepository<Discussion, UUID> {
    Discussion findByDiscussionTopic(String discussionTopic);
}
