package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.request.CourseRatingRequest;
import com.binar.byteacademy.dto.response.CourseRatingResponse;
import com.binar.byteacademy.dto.response.MyCourseResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.service.CourseRatingService;
import com.binar.byteacademy.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.binar.byteacademy.common.util.Constants.CoursePats.CUSTOMER_COURSE_PATS;

@RestController
@RequestMapping(value = CUSTOMER_COURSE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer Course", description = "Customer Course API")
public class CustomerCourseController {
    private final CourseService courseService;
    private final CourseRatingService courseRatingService;

    @GetMapping
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
        Page<MyCourseResponse> courseResponsePage = courseService.getMyCourseList(
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

    @PostMapping("/rating")
    @Schema(name = "AddCourseRating", description = "Add course rating")
    @Operation(summary = "Endpoint to handle add course rating (User Role : Customer)")
    public ResponseEntity<APIResultResponse<CourseRatingResponse>> addCourseRating(
            @RequestBody @Valid CourseRatingRequest request,
            Principal connectedUser) {
        CourseRatingResponse courseRatingResponse = courseRatingService.addCourseRating(request, connectedUser);
        APIResultResponse<CourseRatingResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Course rating successfully added",
                courseRatingResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
