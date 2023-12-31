package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.CreateCategoryRequest;
import com.binar.byteacademy.dto.request.UpdateCategoryRequest;
import com.binar.byteacademy.dto.response.CategoryResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.CategoryService;
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

import static com.binar.byteacademy.common.util.Constants.CategoryPats.ADMIN_CATEGORY_PATS;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CATEGORY_SUCCESSFULLY_RETRIEVED;

@RestController
@RequestMapping(value = ADMIN_CATEGORY_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Category", description = "Admin Category API")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Schema(name = "CreateCategoryRequest", description = "Create category request body")
    @Operation(summary = "Endpoint to handle create new category (User Role : Admin)")
    public ResponseEntity<APIResultResponse<CategoryResponse>> createNewCategoryAsync(
            @RequestBody @Valid CreateCategoryRequest request) {
        CompletableFuture<CategoryResponse> futureResult = categoryService.addCategory(request);
        return futureResult.thenApplyAsync(categoryResponse -> {
            APIResultResponse<CategoryResponse> responseDTO = new APIResultResponse<>(
                    HttpStatus.CREATED,
                    CATEGORY_SUCCESSFULLY_RETRIEVED,
                    categoryResponse
            );
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        }).join();
    }

    @PutMapping("/{slugCategory}")
    @Schema(name = "UpdateCategoryRequest", description = "Update category request body")
    @Operation(summary = "Endpoint to handle update category (User Role : Admin)")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable String slugCategory, @RequestBody @Valid UpdateCategoryRequest request) {
        CompletableFuture<CategoryResponse> futureResult = categoryService.updateCategory(slugCategory, request);
        return futureResult.thenApplyAsync(aVoid -> {
            APIResponse responseDTO =  new APIResponse(
                    HttpStatus.OK,
                    "Category successfully updated"
            );
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }).join();
    }

    @DeleteMapping("/{slugCategory}")
    @Schema(name = "GetCategoryDetailRequest", description = "Get category detail request body")
    @Operation(summary = "Endpoint to handle delete category (User Role : Admin)")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable String slugCategory) {
        categoryService.deleteCategory(slugCategory);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Category successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    @Schema(name = "GetAllCategoryRequest", description = "Get all category request body")
    @Operation(summary = "Endpoint to handle get all category (User Role : Admin)")
    public ResponseEntity<APIResultResponse<Page<CategoryResponse>>> getAllCategory(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<CategoryResponse> categoryResponses = categoryService.getAllCategory(pageable);
        APIResultResponse<Page<CategoryResponse>> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                CATEGORY_SUCCESSFULLY_RETRIEVED,
                categoryResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/list")
    @Schema(name = "GetAllCategoryRequest", description = "Get all category request body")
    @Operation(summary = "Endpoint to handle get all category (User Role : Admin)")
    public ResponseEntity<APIResultResponse<List<CategoryResponse>>> getListCategory() {
        List<CategoryResponse> categoryResponses = categoryService.getListCategory();
        APIResultResponse<List<CategoryResponse>> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                CATEGORY_SUCCESSFULLY_RETRIEVED,
                categoryResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{slugCategory}")
    @Schema(name = "GetCategoryDetailRequest", description = "Get category detail request body")
    @Operation(summary = "Endpoint to handle get category detail (User Role : Admin)")
    public ResponseEntity<APIResultResponse<CategoryResponse>> getCategoryDetail(@PathVariable String slugCategory) {
        CategoryResponse categoryResponse = categoryService.getCategoryDetail(slugCategory);
        APIResultResponse<CategoryResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                CATEGORY_SUCCESSFULLY_RETRIEVED,
                categoryResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
