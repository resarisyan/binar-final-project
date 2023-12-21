package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.MaterialActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaterialActivityRepository  extends JpaRepository<MaterialActivity, UUID> {
}
