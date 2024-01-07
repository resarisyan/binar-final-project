package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Comment;
import com.binar.byteacademy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findAllByDiscussion_SlugDiscussion(Pageable pageable, String slugDiscussion);
    Optional<Comment> findByIdAndUser(UUID id, User user);
}