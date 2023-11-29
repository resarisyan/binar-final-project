package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.response.CourseDetailResponse;
import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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

}
