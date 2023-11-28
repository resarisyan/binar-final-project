package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;

import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.specification.CourseFilterSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseSearchFilterServiceImpl implements CourseSearchFilterService {
    private final CourseRepository courseRepository;

    @Override
    public Page<CourseResponse> getAllCourseByCriteria(
            List<String> categoryNames,
            List<EnumCourseLevel> courseLevels,
            List<EnumCourseType> courseTypes,
            List<EnumStatus> courseStatuses,
            List<EnumFilterCoursesBy> filterCoursesBy,
            String keyword,
            Pageable pageable
    ) {
        try {
            categoryNames = Optional.ofNullable(categoryNames)
                    .orElse(Collections.emptyList());
            courseLevels = Optional.ofNullable(courseLevels)
                    .orElse(Collections.emptyList());
            courseTypes = Optional.ofNullable(courseTypes)
                    .orElse(Collections.emptyList());
            courseStatuses = Optional.ofNullable(courseStatuses)
                    .orElse(Collections.emptyList());
            filterCoursesBy = Optional.ofNullable(filterCoursesBy)
                    .orElse(Collections.emptyList());
            Page<Course> coursePage = Optional.of(courseRepository.findAll(
                            CourseFilterSpecification.filterCourses(
                                    categoryNames,
                                    courseLevels,
                                    courseTypes,
                                    courseStatuses,
                                    filterCoursesBy,
                                    keyword
                            ),pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new  DataNotFoundException("Course Not  Found"));
            return coursePage.map(course -> CourseResponse.builder()
                    .courseName(course.getCourseName())
                    .courseSubTitle(course.getCourseSubTitle())
                    .instructorName(course.getInstructorName())
                    .courseLevel(course.getCourseLevel())
                    .courseType(course.getCourseType())
                    .price(course.getPrice())
                    .totalCourseRate(course.getTotalCourseRate())
                    .courseDuration(course.getCourseDuration())
                    .totalModules(course.getTotalModules())
                    .build());
        } catch (DataNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get course with filter");
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }
}
