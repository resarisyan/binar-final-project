package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumRole;
import com.binar.byteacademy.enumeration.EnumStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByUsername(String username);
    Optional<User> findFirstByPhoneNumber(String phoneNumber);
    Optional<User> findFirstByEmail(String email);
    Optional<List<User>> findAllByRole(EnumRole role);

    int countByStatus(EnumStatus status);
}

