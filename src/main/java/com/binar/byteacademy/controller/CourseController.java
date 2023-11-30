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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.CoursePats.COURSE_PATS;

@RestController
@RequestMapping(value = COURSE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Course", description = "Course API")
public class CourseController {
    private final CourseService courseService;

    @GetMapping()
    @Schema(name = "List Courses", description = "Get List of Courses with course type and see the details course")
    @Operation(summary = "Endpoint to get a list of courses and details course")
    public ResponseEntity<APIResultResponse<?>> listCourses(
            @Valid @RequestParam(value = "type", required = false) EnumCourseType type,
            @Valid @RequestParam(value = "id", required = false) UUID courseId) {

        if (type == null && courseId == null) {
            List<CourseResponse> courseResponses = courseService.listCourses();
            return new ResponseEntity<>(
                    new APIResultResponse<>(HttpStatus.OK, "List of courses retrieved successfully", courseResponses),
                    HttpStatus.OK
            );
        } else if (type != null && courseId != null) {
            Optional<CourseDetailResponse> courseDetailResponse = courseService.courseDetail(type, courseId);
            return courseDetailResponse.<ResponseEntity<APIResultResponse<?>>>map(detailResponse -> new ResponseEntity<>(
                    new APIResultResponse<>(HttpStatus.OK, "Course details retrieved successfully", detailResponse),
                    HttpStatus.OK
            )).orElseGet(() -> new ResponseEntity<>(
                    new APIResultResponse<>(HttpStatus.NOT_FOUND, "Course not found", null),
                    HttpStatus.NOT_FOUND
            ));
        } else if (type != null) {
            List<CourseResponse> courseResponses = courseService.listCoursesByCourseType(type);
            APIResultResponse<List<CourseResponse>> responseDTO = new APIResultResponse<>(
                    HttpStatus.OK,
                    "List of courses retrieved successfully",
                    courseResponses
            );
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new APIResultResponse<>(HttpStatus.BAD_REQUEST, "Invalid combination of parameters", null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/search")
    @Schema(name = "GetActiveCourseByCriteria", description = "Get active course by criteria")
    @Operation(summary = "Endpoint to handle get active course by criteria")
    public ResponseEntity<APIResultResponse<Page<SearchCourseResponse>>> getActiveCourseByCriteria(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryName", required = false) List<String> categoryNames,
            @RequestParam(value = "courseLevels", required = false) List<EnumCourseLevel> courseLevels,
            @RequestParam(value = "courseType", required = false) List<EnumCourseType> courseTypes,
            @RequestParam(value = "filterCoursesBy", required = false) List<EnumFilterCoursesBy> filterCoursesBy,
            @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page,6);
        List<EnumStatus> courseStatuses = List.of(EnumStatus.ACTIVE);
        Page<SearchCourseResponse> courseResponsePage = courseService.getCourseListForWeb(
                categoryNames,
                courseLevels,
                courseTypes,
                courseStatuses,
                filterCoursesBy,
                keyword,
                pageable);
        APIResultResponse<Page<SearchCourseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Course successfully retrieved",
                courseResponsePage
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
