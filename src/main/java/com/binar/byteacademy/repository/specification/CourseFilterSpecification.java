package com.binar.byteacademy.repository.specification;

import com.binar.byteacademy.entity.*;
import com.binar.byteacademy.enumeration.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseFilterSpecification {
    public static Specification<Course> filterCourses(
            List<String> categoryNames,
            List<EnumCourseLevel> courseLevels,
            List<EnumCourseType> courseTypes,
            List<EnumStatus> courseStatuses,
            List<EnumFilterCoursesBy> filterCoursesBy,
            String keyword,
            String username
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> filterByPredicates = new ArrayList<>();
            if (!categoryNames.isEmpty()) {
                Join<Course, Category> categoryJoin = root.join("category", JoinType.LEFT);
                predicates.add(categoryJoin.get("categoryName").in(categoryNames));
            }
            if (!courseLevels.isEmpty()) {
                predicates.add(root.get("courseLevel").in(courseLevels));
            }
            if (!courseTypes.isEmpty()) {
                predicates.add(root.get("courseType").in(courseTypes));
            }
            if (!courseStatuses.isEmpty()) {
                predicates.add(root.get("courseStatus").in(courseStatuses));
            }
            if (keyword != null) {
                predicates.add(criteriaBuilder.like(root.get("courseName"), "%"+keyword.toLowerCase()+"%"));
            }
            if (username != null) {
                Join<Course, Purchase> purchaseJoin = root.join("purchases");
                Join<Course, UserProgress> userProgressJoin = root.join("userProgresses");
                Join<Purchase, User> userJoin1 = purchaseJoin.join("user");
                Join<UserProgress, User> userJoin2 = userProgressJoin.join("user");
                predicates.add(criteriaBuilder.equal(purchaseJoin.get("purchaseStatus"), EnumPurchaseStatus.PAID));
                predicates.add(criteriaBuilder.equal(userJoin1.get("username"), username));
                predicates.add(criteriaBuilder.equal(userJoin2.get("username"), username));
            }
            if (filterCoursesBy.contains(EnumFilterCoursesBy.PROMO)) {
                Join<Course, Promo> promoJoin = root.join("promos", JoinType.LEFT);
                filterByPredicates.add(criteriaBuilder.isNotNull(promoJoin.get("id")));
            }
            if (filterCoursesBy.contains(EnumFilterCoursesBy.LATEST)) {
                LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
                filterByPredicates.add(criteriaBuilder.greaterThan(root.get("createdAt"), oneMonthAgo));
            }
            if (filterCoursesBy.contains(EnumFilterCoursesBy.MOST_POPULAR)) {
                filterByPredicates.add(criteriaBuilder.greaterThan(root.get("totalCourseRate"), 4.5));
            }
            if (!filterCoursesBy.isEmpty()) {
                predicates.add(criteriaBuilder.or(filterByPredicates.toArray(new Predicate[0])));
            }
            if(filterCoursesBy.contains(EnumFilterCoursesBy.PROMO)) {
                query.distinct(true).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            } else {
                query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            }
            return query.getRestriction();
        });
    }
}