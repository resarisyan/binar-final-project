package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.response.CategoryResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.binar.byteacademy.common.util.Constants.CategoryPats.CATEGORY_PATS;

@RestController
@RequestMapping(value = CATEGORY_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category API")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Schema(name = "GetCategoryList", description = "Get category list")
    @Operation(summary = "Endpoint to handle get category list")
    public ResponseEntity<APIResultResponse<Page<CategoryResponse>>> getCategoryList(@RequestParam(value = "page") int page) {
        Pageable pageable = Pageable.ofSize(6).withPage(page);
        Page<CategoryResponse> categoryResponsePage = categoryService.getAllCategory(pageable);
        APIResultResponse<Page<CategoryResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success get all category",
                categoryResponsePage
        );
        return ResponseEntity.ok(responseDTO);
    }
}
