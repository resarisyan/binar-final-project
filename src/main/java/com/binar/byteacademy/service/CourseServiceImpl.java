package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.*;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    private static final String COURSE_NOT_FOUND = "Course not found";

    @Override
    public List<CourseResponse> listCourses() {
        try {
            log.info("Fetching all courses");
            List<Course> courses = Optional.of(courseRepository.findAll())
                    .orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));

            return courses.stream()
                    .map(course -> {
                        CategoryResponse categoryResponse = CategoryResponse.builder()
                                .categoryId(course.getCategory().getId())
                                .categoryName(course.getCategory().getCategoryName())
                                .pathCategoryImage(course.getCategory().getPathCategoryImage())
                                .build();

                        return CourseResponse.builder()
                                .id(course.getId())
                                .courseName(course.getCourseName())
                                .instructorName(course.getInstructorName())
                                .price(course.getPrice())
                                .totalCourseRate(course.getTotalCourseRate())
                                .totalModules(course.getTotalModules())
                                .courseDuration(course.getCourseDuration())
                                .slugCourse(course.getCourseName().toLowerCase().replaceAll("\\s", "-"))
                                .courseType(course.getCourseType())
                                .pathCourseImage(course.getPathCourseImage())
                                .courseLevel(course.getCourseLevel())
                                .category(categoryResponse)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (DataNotFoundException e) {
            log.error("No courses found");
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all courses", e);
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public List<CourseResponse> listCoursesByCourseType(EnumCourseType courseType) {
        try {
            log.info("Fetching courses by course type: {}", courseType);
            List<Course> courses = Optional.of(courseRepository.findByCourseType(courseType))
                    .orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));

            return courses.stream()
                    .map(course -> {
                        CategoryResponse categoryResponse = CategoryResponse.builder()
                                .categoryId(course.getCategory().getId())
                                .categoryName(course.getCategory().getCategoryName())
                                .pathCategoryImage(course.getCategory().getPathCategoryImage())
                                .build();

                        return CourseResponse.builder()
                                .id(course.getId())
                                .courseName(course.getCourseName())
                                .instructorName(course.getInstructorName())
                                .price(course.getPrice())
                                .totalCourseRate(course.getTotalCourseRate())
                                .totalModules(course.getTotalModules())
                                .courseDuration(course.getCourseDuration())
                                .slugCourse(course.getCourseName().toLowerCase().replaceAll("\\s", "-"))
                                .courseType(course.getCourseType())
                                .pathCourseImage(course.getPathCourseImage())
                                .courseLevel(course.getCourseLevel())
                                .category(categoryResponse)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (DataNotFoundException e) {
            log.error("No courses found for course type: {}", courseType);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get courses by course type: {}", courseType, e);
            throw new ServiceBusinessException(e.getMessage());
        }
    }


    @Override
    public Optional<CourseDetailResponse> courseDetail(EnumCourseType courseType, UUID courseId) {
        try {
            log.info("Fetching course detail by course type: {} and id: {}", courseType, courseId);

            Optional<Course> course = courseRepository.findByCourseTypeAndId(courseType, courseId);

            return course.map(c -> {
                List<ChapterResponse> chapterResponses = c.getChapters().stream()
                        .map(chapter -> {
                            List<MaterialResponse> materialResponses = chapter.getMaterials().stream()
                                    .map(material -> MaterialResponse.builder()
                                            .materialId(material.getId())
                                            .serialNumber(material.getSerialNumber())
                                            .videoLink(material.getVideoLink())
                                            .materialDuration(material.getMaterialDuration())
                                            .slugMaterial(material.getSlugMaterial())
                                            .materialType(material.getMaterialType())
                                            .build())
                                    .collect(Collectors.toList());

                            return ChapterResponse.builder()
                                    .chapterId(chapter.getId())
                                    .noChapter(chapter.getNoChapter())
                                    .title(chapter.getTitle())
                                    .chapterDuration(chapter.getChapterDuration())
                                    .materials(materialResponses)
                                    .build();
                        })
                        .collect(Collectors.toList());

                CategoryResponse categoryResponse = CategoryResponse.builder()
                        .categoryId(c.getCategory().getId())
                        .categoryName(c.getCategory().getCategoryName())
                        .pathCategoryImage(c.getCategory().getPathCategoryImage())
                        .build();

                return CourseDetailResponse.builder()
                        .id(c.getId())
                        .courseName(c.getCourseName())
                        .instructorName(c.getInstructorName())
                        .totalCourseRate(c.getTotalCourseRate())
                        .totalModules(c.getTotalModules())
                        .courseDuration(c.getCourseDuration())
                        .groupLink(c.getGroupLink())
                        .slugCourse(c.getSlugCourse())
                        .courseType(c.getCourseType())
                        .pathCourseImage(c.getPathCourseImage())
                        .courseLevel(c.getCourseLevel())
                        .listChapter(chapterResponses)
                        .category(categoryResponse)
                        .build();
            });
        } catch (DataNotFoundException e) {
            log.error("No courses found for course type: {} and id: {}", courseType, courseId);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get course detail by course type: {} and id: {}", courseType, courseId, e);
            throw new ServiceBusinessException(e.getMessage());
        }
    }



}
