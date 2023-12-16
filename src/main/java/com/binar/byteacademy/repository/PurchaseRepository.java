package com.binar.byteacademy.repository;

import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.Purchase;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    Optional<Purchase> findFirstBySlugPurchase(String slugPurchase);

    Optional<Purchase> findByUserAndCourseAndPurchaseStatus(User user, Course course, EnumPurchaseStatus purchaseStatus);

    @Query(value = """
            SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Purchase p
            WHERE p.user = :user
            AND p.course = :course
            AND p.purchaseStatus IN :statuses
            """)
    boolean existsByUserAndCourseAndPurchaseStatusIn(User user, Course course, List<EnumPurchaseStatus> statuses);

    @Query(value = """
            SELECT p FROM Purchase p
            ORDER BY CASE WHEN p.purchaseStatus = :purchaseStatus THEN 0 ELSE 1 END, p.purchaseStatus
            """)
    Page<Purchase> findAllOrderByFirstPurchaseStatus(EnumPurchaseStatus purchaseStatus, Pageable pageable);

    @Query(value = """
            SELECT p FROM Purchase p LEFT JOIN p.user u
            WHERE u.username = :username
            ORDER BY CASE WHEN p.purchaseStatus = :purchaseStatus THEN 0 ELSE 1 END, p.purchaseStatus
            """)
    Page<Purchase> findAllByUsernameOrderByFirstPurchaseStatus(String username, EnumPurchaseStatus purchaseStatus, Pageable pageable);
}
