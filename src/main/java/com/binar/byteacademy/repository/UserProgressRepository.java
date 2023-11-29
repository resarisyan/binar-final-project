package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.UserProgress;
import com.binar.byteacademy.entity.compositekey.UserProgressKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, UserProgressKey> {
}
