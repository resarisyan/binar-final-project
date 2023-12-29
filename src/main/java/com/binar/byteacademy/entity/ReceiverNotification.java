package com.binar.byteacademy.entity;

import com.binar.byteacademy.enumeration.EnumNotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receiver_notifications")
public class ReceiverNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "receiver_notification_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EnumNotificationStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id", referencedColumnName = "notification_id", nullable = false)
    private Notification notification;
}
