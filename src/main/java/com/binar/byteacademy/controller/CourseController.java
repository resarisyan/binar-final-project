package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.response.*;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.service.CourseService;
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
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.SUCCESS_GET_COURSE;
@RestController
@RequestMapping(value = COURSE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Course", description = "Course API")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/{slugCourse}")
    @Schema(name = "GetCourseDetail", description = "Get course detail")
    @Operation(summary = "Endpoint to handle get course detail")
    public ResponseEntity<APIResultResponse<CustomerCourseDetailResponse>> getCourseDetail(@PathVariable String slugCourse) {
        CustomerCourseDetailResponse courseDetailResponse = courseService.getCustomerCourseDetail(slugCourse);
        APIResultResponse<CustomerCourseDetailResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                SUCCESS_GET_COURSE,
                courseDetailResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

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
        Page<CourseResponse> courseResponsePage = courseService.getCourseListForWeb(
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
}