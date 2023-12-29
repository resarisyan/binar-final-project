package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.NotificationRequest;
import com.binar.byteacademy.dto.response.NotificationResponse;
import com.binar.byteacademy.dto.response.ReceiverNotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.UUID;

public interface NotificationService {
    NotificationResponse sendToAll(NotificationRequest request);
    NotificationResponse sendToUser(NotificationRequest request, String username);
    Page<ReceiverNotificationResponse> getAllNotification(Pageable pageable, Principal connectedUser);
    ReceiverNotificationResponse readNotification(UUID id, Principal connectedUser);
    int countUnreadNotification(Principal connectedUser);
}
