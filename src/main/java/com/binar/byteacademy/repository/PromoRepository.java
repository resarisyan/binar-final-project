package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromoRepository extends JpaRepository<Promo, UUID> {
}
