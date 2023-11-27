package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.binar.byteacademy.common.util.Constants.CoursePats.COURSE_PATS;

@RestController
@RequestMapping(value = COURSE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Course", description = "Course API")
public class CourseController {

    @Autowired
    private final CourseService courseService;

    @GetMapping()
    @Schema(name = "ListCourses", description = "Get List of Courses")
    @Operation(summary = "Endpoint to get a list of courses")
    public ResponseEntity<APIResultResponse<List<CourseResponse>>> listCourses(@RequestParam(required = false) EnumCourseType type) {
        List<CourseResponse> courseResponses = new ArrayList<>();
        if (type == null) {
            courseResponses.addAll(courseService.listCourses());
        } else {
            courseResponses.addAll(courseService.listCoursesByCourseType(type));
        }
        APIResultResponse<List<CourseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "List of courses retrieved successfully",
                courseResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
