package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.ReceiverNotification;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumNotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReceiverNotificationRepository extends JpaRepository<ReceiverNotification, UUID> {
    Page<ReceiverNotification> findAllByUser(User user, Pageable pageable);
    Optional<ReceiverNotification> findByIdAndUser(UUID id, User user);
    Integer countByUserAndStatus(User user, EnumNotificationStatus status);
}
