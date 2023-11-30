package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.*;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Page<CourseResponse> getListCourses(Pageable pageable) {
        try {
            Page<Course> coursePage = Optional.of(courseRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException("Course Not  Found"));
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
                    .slugCourse(course.getSlugCourse())
                    .pathCourseImage(course.getPathCourseImage())
                    .category(CategoryResponse.builder()
                            .categoryName(course.getCategory().getCategoryName())
                            .pathCategoryImage(course.getCategory().getPathCategoryImage())
                            .build())
                    .build());
        } catch (DataNotFoundException e) {
            log.error("No courses found");
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all courses", e);
            throw new ServiceBusinessException(e.getMessage());
        }
    }

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
                            ), pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException("Course Not  Found"));
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
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }

    @Override
    public CourseDetailResponse getCourseDetail(String courseSlug) {
        try {
            Course course = courseRepository.findFirstBySlugCourse(courseSlug)
                    .orElseThrow(
                            () -> new DataNotFoundException("Course not found")
                    );
            return CourseDetailResponse.builder()
                    .courseName(course.getCourseName())
                    .instructorName(course.getInstructorName())
                    .totalCourseRate(course.getTotalCourseRate())
                    .totalModules(course.getTotalModules())
                    .courseDuration(course.getCourseDuration())
                    .groupLink(course.getGroupLink())
                    .slugCourse(course.getSlugCourse())
                    .courseType(course.getCourseType())
                    .pathCourseImage(course.getPathCourseImage())
                    .courseLevel(course.getCourseLevel())
                    .category(CategoryResponse.builder()
                            .categoryName(course.getCategory().getCategoryName())
                            .pathCategoryImage(course.getCategory().getPathCategoryImage())
                            .build())
                    .chapters(course.getChapters().stream()
                            .map(chapter -> ChapterCourseDetailResponse.builder()
                                    .title(chapter.getTitle())
                                    .chapterDuration(chapter.getChapterDuration())
                                    .noChapter(chapter.getNoChapter())
                                    .materials(chapter.getMaterials().stream()
                                            .map(material -> MaterialCourseDetailResponse.builder()
                                                    .materialType(material.getMaterialType())
                                                    .materialDuration(material.getMaterialDuration())
                                                    .slugMaterial(material.getSlugMaterial())
                                                    .build())
                                            .collect(Collectors.toList()))
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get course detail");
        }
    }
}
