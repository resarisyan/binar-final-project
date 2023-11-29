package com.binar.byteacademy.repository.specification;

import com.binar.byteacademy.entity.Category;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.Promo;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
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
            String keyword
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
                predicates.add(criteriaBuilder.like(root.get("courseName"), "%"+keyword+"%"));
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
            query.distinct(true).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            return query.getRestriction();
        });
    }
}
