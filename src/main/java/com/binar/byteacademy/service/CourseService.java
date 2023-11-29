package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.CourseDetailResponse;
import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.enumeration.EnumCourseType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    List<CourseResponse> listCourses();

    List<CourseResponse> listCoursesByCourseType(EnumCourseType courseType);

    Optional<CourseDetailResponse> courseDetail(EnumCourseType courseType, UUID courseId);
}
