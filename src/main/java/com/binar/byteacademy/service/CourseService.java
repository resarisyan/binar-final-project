package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.enumeration.EnumCourseType;

import java.util.List;

public interface CourseService {

    List<CourseResponse> listCourses();

    List<CourseResponse> listCoursesByCourseType(EnumCourseType courseType);
}
