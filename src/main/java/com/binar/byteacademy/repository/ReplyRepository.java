package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Reply;
import com.binar.byteacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReplyRepository extends JpaRepository<Reply, UUID> {
    Optional<Reply> findByIdAndUser(UUID id, User user);
}