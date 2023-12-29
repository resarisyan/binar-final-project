package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.NotificationRequest;
import com.binar.byteacademy.dto.response.NotificationResponse;
import com.binar.byteacademy.dto.response.ReceiverNotificationResponse;
import com.binar.byteacademy.entity.Notification;
import com.binar.byteacademy.entity.ReceiverNotification;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumNotificationStatus;
import com.binar.byteacademy.enumeration.EnumRole;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.NotificationRepository;
import com.binar.byteacademy.repository.ReceiverNotificationRepository;
import com.binar.byteacademy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final ReceiverNotificationRepository receiverNotificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public NotificationResponse sendToAll(NotificationRequest request) {
        try {
            List<ReceiverNotification> receiverNotifications = new ArrayList<>();
            Notification notification = notificationRepository.save(
                    Notification.builder()
                            .title(request.getTitle())
                            .body(request.getBody())
                            .build()
            );
            userRepository.findAllByRole(EnumRole.CUSTOMER).ifPresentOrElse(users -> users.forEach(user -> {
                log.info("User: {}", user.getUsername());
                ReceiverNotification receiverNotification = ReceiverNotification.builder()
                        .notification(notification)
                        .user(user)
                        .status(EnumNotificationStatus.UNREAD)
                        .build();
                receiverNotifications.add(receiverNotification);
            }), () -> {
                throw new DataNotFoundException(USER_NOT_FOUND);
            });
            receiverNotificationRepository.saveAll(receiverNotifications);
            NotificationResponse notificationResponse = NotificationResponse.builder()
                    .title(notification.getTitle())
                    .body(notification.getBody())
                    .createdAt(notification.getCreatedAt())
                    .build();
            messagingTemplate.convertAndSend("/all/notifications", notificationResponse);
            return notificationResponse;
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(USER_NOT_FOUND);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to send notification");
        }
    }

    @Override
    @Transactional
    public NotificationResponse sendToUser(NotificationRequest request, String username) {
        try {
            Notification notification = notificationRepository.save(
                    Notification.builder()
                            .title(request.getTitle())
                            .body(request.getBody())
                            .build()
            );
            return userRepository.findFirstByUsername(username).map(
                    user -> {
                        ReceiverNotification receiverNotification = ReceiverNotification.builder()
                                .notification(notification)
                                .user(user)
                                .status(EnumNotificationStatus.UNREAD)
                                .build();
                        receiverNotificationRepository.save(receiverNotification);
                        NotificationResponse notificationResponse = NotificationResponse.builder()
                                .title(notification.getTitle())
                                .body(notification.getBody())
                                .createdAt(notification.getCreatedAt())
                                .build();
                        messagingTemplate.convertAndSendToUser(user.getUsername(), "/specific/notifications", notificationResponse);
                        return notificationResponse;
                    }
            ).orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(USER_NOT_FOUND);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to send notification");
        }
    }

    @Override
    public Page<ReceiverNotificationResponse> getAllNotification(Pageable pageable, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            Page<ReceiverNotification> receiverNotifications = Optional.ofNullable(
                    receiverNotificationRepository.findAllByUser(user, pageable))
                    .orElseThrow(() -> new DataNotFoundException("No notification found")
                    );
            return receiverNotifications.map(receiverNotification -> ReceiverNotificationResponse.builder()
                    .id(receiverNotification.getId())
                    .notification(NotificationResponse.builder()
                            .title(receiverNotification.getNotification().getTitle())
                            .body(receiverNotification.getNotification().getBody())
                            .createdAt(receiverNotification.getNotification().getCreatedAt())
                            .build())
                    .status(receiverNotification.getStatus())
                    .build());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(USER_NOT_FOUND);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get notification");
        }
    }

    @Override
    @Transactional
    public ReceiverNotificationResponse readNotification(UUID id, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            return receiverNotificationRepository.findByIdAndUser(id, user)
                    .map(receiverNotification -> {
                        receiverNotification.setStatus(EnumNotificationStatus.READ);
                        receiverNotificationRepository.save(receiverNotification);
                        return ReceiverNotificationResponse.builder()
                                .notification(NotificationResponse.builder()
                                        .title(receiverNotification.getNotification().getTitle())
                                        .body(receiverNotification.getNotification().getBody())
                                        .createdAt(receiverNotification.getNotification().getCreatedAt())
                                        .build())
                                .status(receiverNotification.getStatus())
                                .build();
                    }).orElseThrow(() -> new DataNotFoundException("Notification not found"));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(USER_NOT_FOUND);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to read notification");
        }
    }

    @Override
    public int countUnreadNotification(Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            return receiverNotificationRepository.countByUserAndStatus(user, EnumNotificationStatus.UNREAD);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(USER_NOT_FOUND);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to count unread notification");
        }
    }
}
