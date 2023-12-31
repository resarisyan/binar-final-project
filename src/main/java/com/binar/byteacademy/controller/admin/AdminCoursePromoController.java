package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.CoursePromoRequest;
import com.binar.byteacademy.dto.response.CoursePromoResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.CoursePromoService;
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

import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.CoursePromoPats.ADMIN_COURSE_PROMO_PATS;

@RestController
@RequestMapping(value = ADMIN_COURSE_PROMO_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Course Promo", description = "Admin Course Promo")
public class AdminCoursePromoController {
    private final CoursePromoService coursePromoService;

    @PostMapping
    @Schema(name = "CoursePromoRequest", description = "Create course promo request body")
    @Operation(summary = "Endpoint to handle create new course promo (User Role : Admin)")
    public ResponseEntity<APIResultResponse<CoursePromoResponse>> createNewPromo(
            @RequestBody @Valid CoursePromoRequest request) {
        CoursePromoResponse coursePromoResponse = coursePromoService.addCoursePromo(request);
        APIResultResponse<CoursePromoResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Promo successfully created",
                coursePromoResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Schema(name = "CoursePromoRequest", description = "Update course promo request body")
    @Operation(summary = "Endpoint to handle update course promo (User Role : Admin)")
    public ResponseEntity<APIResponse> updatePromo(@PathVariable UUID id, @RequestBody @Valid CoursePromoRequest request) {
        coursePromoService.updateCoursePromo(id, request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Promo successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Schema(name = "CoursePromoRequest", description = "Delete course promo request body")
    @Operation(summary = "Endpoint to handle delete course promo (User Role : Admin)")
    public ResponseEntity<APIResponse> deletePromo(@PathVariable UUID id) {
        coursePromoService.deleteCoursePromo(id);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Promo successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    @Schema(name = "CoursePromoRequest", description = "Get course promo request body")
    @Operation(summary = "Endpoint to handle get course promo (User Role : Admin)")
    public ResponseEntity<APIResultResponse<Page<CoursePromoResponse>>> getPromo(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<CoursePromoResponse> coursePromoResponses = coursePromoService.getAllCoursePromo(pageable);
        APIResultResponse<Page<CoursePromoResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Promo successfully retrieved",
                coursePromoResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Schema(name = "CoursePromoRequest", description = "Get course promo request body")
    @Operation(summary = "Endpoint to handle get course promo detail (User Role : Admin)")
    public ResponseEntity<APIResultResponse<CoursePromoResponse>> getPromoDetail(@PathVariable UUID id) {
        CoursePromoResponse coursePromoResponse = coursePromoService.getCoursePromoDetail(id);
        APIResultResponse<CoursePromoResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Promo successfully retrieved",
                coursePromoResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
