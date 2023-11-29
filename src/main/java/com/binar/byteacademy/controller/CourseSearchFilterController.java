package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.response.AdminCourseResponse;
import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.dto.response.MyCourseResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.service.CourseSearchFilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binar.byteacademy.common.util.Constants.CoursePats.COURSE_PATS;

@RestController
@RequestMapping(value = COURSE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Course Search & Filter", description = "Course Search & Filter API")
public class CourseSearchFilterController {
    private final CourseSearchFilterService courseSearchFilterService;

    @GetMapping("/search")
    @Schema(name = "GetActiveCourseByCriteria", description = "Get active course by criteria")
    @Operation(summary = "Endpoint to handle get active course by criteria")
    public ResponseEntity<APIResultResponse<Page<CourseResponse>>> getActiveCourseByCriteria(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryName", required = false) List<String> categoryNames,
            @RequestParam(value = "courseLevels", required = false) List<EnumCourseLevel> courseLevels,
            @RequestParam(value = "courseType", required = false) List<EnumCourseType> courseTypes,
            @RequestParam(value = "filterCoursesBy", required = false) List<EnumFilterCoursesBy> filterCoursesBy,
            @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page,6);
        List<EnumStatus> courseStatuses = List.of(EnumStatus.ACTIVE);
        Page<CourseResponse> courseResponsePage = courseSearchFilterService.getCourseListForWeb(
                categoryNames,
                courseLevels,
                courseTypes,
                courseStatuses,
                filterCoursesBy,
                keyword,
                pageable);
        APIResultResponse<Page<CourseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Course successfully retrieved",
                courseResponsePage
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Schema(name = "GetAllCourseByCriteria", description = "Get all course by criteria")
    @Operation(summary = "Endpoint to handle get all course by criteria (User Role : Admin)")
    public ResponseEntity<APIResultResponse<Page<AdminCourseResponse>>> getAllCourseByCriteria(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryName", required = false) List<String> categoryNames,
            @RequestParam(value = "courseLevels", required = false) List<EnumCourseLevel> courseLevels,
            @RequestParam(value = "courseType", required = false) List<EnumCourseType> courseTypes,
            @RequestParam(value = "courseStatuses", required = false) List<EnumStatus> courseStatuses,
            @RequestParam(value = "filterCoursesBy", required = false) List<EnumFilterCoursesBy> filterCoursesBy,
            @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page,6);
        Page<AdminCourseResponse> courseResponsePage = courseSearchFilterService.getCourseListForAdmin(
                categoryNames,
                courseLevels,
                courseTypes,
                courseStatuses,
                filterCoursesBy,
                keyword,
                pageable);
        APIResultResponse<Page<AdminCourseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Course successfully retrieved",
                courseResponsePage
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/my-course")
    @Schema(name = "GetMyCourseByCriteria", description = "Get my course by criteria")
    @Operation(summary = "Endpoint to handle get my course by criteria (User Role : Customer)")
    public ResponseEntity<APIResultResponse<Page<MyCourseResponse>>> getMyCourseByCriteria(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryName", required = false) List<String> categoryNames,
            @RequestParam(value = "courseLevels", required = false) List<EnumCourseLevel> courseLevels,
            @RequestParam(value = "courseType", required = false) List<EnumCourseType> courseTypes,
            @RequestParam(value = "filterCoursesBy", required = false) List<EnumFilterCoursesBy> filterCoursesBy,
            @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page,6);
        List<EnumStatus> courseStatuses = List.of(EnumStatus.ACTIVE);
        Page<MyCourseResponse> courseResponsePage = courseSearchFilterService.getMyCourseList(
                categoryNames,
                courseLevels,
                courseTypes,
                courseStatuses,
                filterCoursesBy,
                keyword,
                pageable);
        APIResultResponse<Page<MyCourseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Course successfully retrieved",
                courseResponsePage
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
