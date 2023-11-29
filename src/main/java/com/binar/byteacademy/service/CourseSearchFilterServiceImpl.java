package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.dto.response.AdminCourseResponse;
import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.dto.response.MyCourseResponse;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.User;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseSearchFilterServiceImpl implements CourseSearchFilterService {
    private final CourseRepository courseRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Page<Course> getAllCourseByCriteria(
            List<String> categoryNames,
            List<EnumCourseLevel> courseLevels,
            List<EnumCourseType> courseTypes,
            List<EnumStatus> courseStatuses,
            List<EnumFilterCoursesBy> filterCoursesBy,
            String keyword,
            String username,
            Pageable pageable) throws DataNotFoundException {
        categoryNames = Optional.ofNullable(categoryNames)
                .map(val -> val.stream().map(String::toLowerCase).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        courseLevels = Optional.ofNullable(courseLevels)
                .orElse(Collections.emptyList());
        courseTypes = Optional.ofNullable(courseTypes)
                .orElse(Collections.emptyList());
        courseStatuses = Optional.ofNullable(courseStatuses)
                .orElse(Collections.emptyList());
        filterCoursesBy = Optional.ofNullable(filterCoursesBy)
                .orElse(Collections.emptyList());
        return Optional.of(courseRepository.findAll(
                        CourseFilterSpecification.filterCourses(
                                categoryNames,
                                courseLevels,
                                courseTypes,
                                courseStatuses,
                                filterCoursesBy,
                                keyword,
                                username
                        ),pageable))
                .filter(Page::hasContent)
                .orElseThrow(() -> new  DataNotFoundException("Course Not  Found"));
    }

    @Override
    public Page<CourseResponse> getCourseListForWeb(
            List<String> categoryNames,
            List<EnumCourseLevel> courseLevels,
            List<EnumCourseType> courseTypes,
            List<EnumStatus> courseStatuses,
            List<EnumFilterCoursesBy> filterCoursesBy,
            String keyword,
            Pageable pageable
    ) {
        try {
            Page<Course> coursePage = getAllCourseByCriteria(
                    categoryNames, courseLevels,courseTypes, courseStatuses,
                    filterCoursesBy, keyword, null, pageable);
            return coursePage.map(course -> CourseResponse.builder()
                    .courseName(course.getCourseName())
                    .categoryName(course.getCategory().getCategoryName())
                    .instructorName(course.getInstructorName())
                    .pathImage(course.getPathCourseImage())
                    .price(course.getPrice())
                    .courseType(course.getCourseType())
                    .courseLevel(course.getCourseLevel())
                    .totalCourseRate(course.getTotalCourseRate())
                    .totalModules(course.getTotalModules())
                    .courseDuration(course.getCourseDuration())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get course with filter");
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }

    @Override
    public Page<AdminCourseResponse> getCourseListForAdmin(
            List<String> categoryNames,
            List<EnumCourseLevel> courseLevels,
            List<EnumCourseType> courseTypes,
            List<EnumStatus> courseStatuses,
            List<EnumFilterCoursesBy> filterCoursesBy,
            String keyword,
            Pageable pageable) {
        try {
            Page<Course> coursePage = getAllCourseByCriteria(
                    categoryNames, courseLevels,courseTypes, courseStatuses,
                    filterCoursesBy, keyword, null, pageable);
            return coursePage.map(course -> AdminCourseResponse.builder()
                    .slugCourse(course.getSlugCourse())
                    .courseName(course.getCourseName())
                    .categoryName(course.getCategory().getCategoryName())
                    .price(course.getPrice())
                    .courseType(course.getCourseType())
                    .courseLevel(course.getCourseLevel())
                    .courseStatus(course.getCourseStatus())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get course with filter");
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }

    @Override
    public Page<MyCourseResponse> getMyCourseList(
            List<String> categoryNames,
            List<EnumCourseLevel> courseLevels,
            List<EnumCourseType> courseTypes,
            List<EnumStatus> courseStatuses,
            List<EnumFilterCoursesBy> filterCoursesBy,
            String keyword,
            Pageable pageable) {
        User user = jwtUtil.getUser();
        Page<Course> coursePage = getAllCourseByCriteria(
                categoryNames, courseLevels,courseTypes, courseStatuses,
                filterCoursesBy, keyword, user.getUsername(), pageable);
        return coursePage.map(course -> MyCourseResponse.builder()
                .courseName(course.getCourseName())
                .categoryName(course.getCategory().getCategoryName())
                .courseLevel(course.getCourseLevel())
                .instructorName(course.getInstructorName())
                .pathImage(course.getPathCourseImage())
                .courseLevel(course.getCourseLevel())
                .totalCourseRate(course.getTotalCourseRate())
                .totalModules(course.getTotalModules())
                .courseDuration(course.getCourseDuration())
                .coursePercentage(course.getUserProgresses().get(0).getCoursePercentage())
                .build());
    }
}
