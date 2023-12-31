package com.binar.byteacademy.entity;

import com.binar.byteacademy.enumeration.EnumTokenAccessType;
import com.binar.byteacademy.enumeration.EnumTokenType;
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
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    private EnumTokenType tokenType;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false)
    private EnumTokenAccessType accessType;

    @Column(name = "revoked", nullable = false)
    public boolean revoked;

    @Column(name = "expired", nullable = false)
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public User user;
}

