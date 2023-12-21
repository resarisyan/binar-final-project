package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateCourseRequest;
import com.binar.byteacademy.dto.request.UpdateCourseRequest;
import com.binar.byteacademy.dto.response.*;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CourseService {
    CompletableFuture<CourseResponse> addCourse(CreateCourseRequest request);
    CompletableFuture<Void> updateCourse(String slugCourse, UpdateCourseRequest request);
    void deleteCourse(String slugCourse);
    Page<CourseResponse> getAllCourse(Pageable pageable);
    CourseDetailResponse getCourseDetail(String slugCourse);
    Page<Course> getAllCourseByCriteria(List<String> categoryNames,
                                        List<EnumCourseLevel> courseLevels,
                                        List<EnumCourseType> courseTypes,
                                        List<EnumStatus> courseStatuses,
                                        List<EnumFilterCoursesBy> filterCoursesBy,
                                        String keyword,
                                        String username,
                                        Pageable pageable);
    Page<CourseResponse> getCourseListForWeb(List<String> categoryNames,
                                                   List<EnumCourseLevel> courseLevels,
                                                   List<EnumCourseType> courseTypes,
                                                   List<EnumStatus> courseStatuses,
                                                   List<EnumFilterCoursesBy> filterCoursesBy,
                                                   String keyword,
                                                   Pageable pageable);

    Page<AdminCourseResponse> getCourseListForAdmin(List<String> categoryNames,
                                                    List<EnumCourseLevel> courseLevels,
                                                    List<EnumCourseType> courseTypes,
                                                    List<EnumStatus> courseStatuses,
                                                    List<EnumFilterCoursesBy> filterCoursesBy,
                                                    String keyword,
                                                    Pageable pageable);

    Page<MyCourseResponse> getMyCourseList(List<String> categoryNames,
                                           List<EnumCourseLevel> courseLevels,
                                           List<EnumCourseType> courseTypes,
                                           List<EnumStatus> courseStatuses,
                                           List<EnumFilterCoursesBy> filterCoursesBy,
                                           String keyword,
                                           Pageable pageable);
}