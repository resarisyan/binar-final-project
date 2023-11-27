package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.CourseResponse;
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
                    .map(course -> CourseResponse.builder()
                            .courseName(course.getCourseName())
                            .courseSubTitle(course.getCourseSubTitle())
                            .instructorName(course.getInstructorName())
                            .price(course.getPrice())
                            .courseType(course.getCourseType())
                            .totalCourseRate(course.getTotalCourseRate())
                            .totalModules(course.getTotalModules())
                            .courseDuration(course.getCourseDuration())
                            .build())
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
                    .map(course -> CourseResponse.builder()
                            .courseName(course.getCourseName())
                            .courseSubTitle(course.getCourseSubTitle())
                            .instructorName(course.getInstructorName())
                            .price(course.getPrice())
                            .courseType(course.getCourseType())
                            .courseLevel(course.getCourseLevel())
                            .totalCourseRate(course.getTotalCourseRate())
                            .totalModules(course.getTotalModules())
                            .courseDuration(course.getCourseDuration())
                            .build())
                    .collect(Collectors.toList());
        } catch (DataNotFoundException e) {
            log.error("No courses found for course type: {}", courseType);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get courses by course type: {}", courseType, e);
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}
