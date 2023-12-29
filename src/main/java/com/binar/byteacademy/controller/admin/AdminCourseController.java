package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.CreateCourseRequest;
import com.binar.byteacademy.dto.request.UpdateCourseRequest;
import com.binar.byteacademy.dto.response.AdminCourseResponse;
import com.binar.byteacademy.dto.response.AdminCourseDetailResponse;
import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.binar.byteacademy.common.util.Constants.CoursePats.ADMIN_COURSE_PATS;

@RestController
@RequestMapping(value = ADMIN_COURSE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Course Admin", description = "Course API Admin")
public class  AdminCourseController {
    private final CourseService courseService;

    @PostMapping
    @Schema(name = "CreateCourseRequest", description = "Create course request body")
    @Operation(summary = "Endpoint to handle create new course (User Role : Admin)")
    public ResponseEntity<APIResultResponse<CourseResponse>> createNewCourse(@RequestBody @Valid CreateCourseRequest request) {
        CompletableFuture<CourseResponse> futureResult = courseService.addCourse(request);
        return futureResult.thenApplyAsync(courseResponse -> {
            APIResultResponse<CourseResponse> responseDTO = new APIResultResponse<>(
                    HttpStatus.CREATED,
                    "Course successfully created",
                    courseResponse
            );
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        }).join();
    }


    @PutMapping("/{courseSlug}")
    @Schema(name = "UpdateCourseRequest", description = "Update course request body")
    @Operation(summary = "Endpoint to handle update course (User Role : Admin)")
    public ResponseEntity<APIResponse> updateCourse(@PathVariable String courseSlug, @RequestBody @Valid UpdateCourseRequest request) {
        CompletableFuture<Void> futureResult = courseService.updateCourse(courseSlug, request);
        return futureResult.thenApplyAsync(aVoid -> {
            APIResponse responseDTO =  new APIResponse(
                    HttpStatus.OK,
                    "Course successfully updated"
            );
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }).join();
    }

    @DeleteMapping("/{slugCourse}")
    @Schema(name = "DeleteCourse", description = "Delete course")
    @Operation(summary = "Endpoint to handle delete course (User Role : Admin)")
    public ResponseEntity<APIResponse> deleteCourse(@PathVariable String slugCourse) {
        courseService.deleteCourse(slugCourse);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Course successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{slugCourse}")
    @Schema(name = "GetAdminCourseDetail", description = "Get course detail")
    @Operation(summary = "Endpoint to handle get course detail (User Role : Admin)")
    public ResponseEntity<APIResultResponse<AdminCourseDetailResponse>> getAdminCourseDetail(@PathVariable String slugCourse) {
        AdminCourseDetailResponse courseResponse = courseService.getAdminCourseDetail(slugCourse);
        APIResultResponse<AdminCourseDetailResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Course successfully retrieved",
                courseResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
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
        Page<AdminCourseResponse> courseResponsePage = courseService.getCourseListForAdmin(
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

    @GetMapping("/list")
    @Schema(name = "GetAllCourseByCriteria", description = "Get all course by criteria")
    @Operation(summary = "Endpoint to handle get all course by criteria (User Role : Admin)")
    public ResponseEntity<APIResultResponse<List<CourseResponse>>> getListCourse() {
        List<CourseResponse> courseResponseList = courseService.getListCourse();
        APIResultResponse<List<CourseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Course successfully retrieved",
                courseResponseList
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
