package com.binar.byteacademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "password_resets")
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "password_reset_id")
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "exp_time", nullable = false)
    LocalDateTime expTime;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
