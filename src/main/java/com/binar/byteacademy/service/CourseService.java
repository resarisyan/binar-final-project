package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.*;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    List<CourseResponse> listCourses();

    List<CourseResponse> listCoursesByCourseType(EnumCourseType courseType);

    Optional<CourseDetailResponse> courseDetail(EnumCourseType courseType, UUID courseId);

    Page<Course> getAllCourseByCriteria(List<String> categoryNames,
                                        List<EnumCourseLevel> courseLevels,
                                        List<EnumCourseType> courseTypes,
                                        List<EnumStatus> courseStatuses,
                                        List<EnumFilterCoursesBy> filterCoursesBy,
                                        String keyword,
                                        String username,
                                        Pageable pageable);
    Page<SearchCourseResponse> getCourseListForWeb(List<String> categoryNames,
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
