package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.Purchase;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    Optional<List<Purchase>> findByCourse(Course course);
    //find by user and order by created add and add pagination
    Page<Purchase> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    Optional<Purchase> findByUserAndCourseAndPurchaseStatus(User user, Course course, EnumPurchaseStatus purchaseStatus);
}
