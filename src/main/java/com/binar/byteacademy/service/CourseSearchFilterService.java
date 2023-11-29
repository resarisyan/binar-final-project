package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseSearchFilterService {
    Page<CourseResponse> getAllCourseByCriteria(List<String> categoryNames,
                                                List<EnumCourseLevel> courseLevels,
                                                List<EnumCourseType> courseTypes,
                                                List<EnumStatus> courseStatuses,
                                                List<EnumFilterCoursesBy> filterCoursesBy,
                                                String keyword,
                                                Pageable pageable);
}
