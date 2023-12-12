package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.CreateCategoryRequest;
import com.binar.byteacademy.dto.request.UpdateCategoryRequest;
import com.binar.byteacademy.dto.response.CategoryResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.binar.byteacademy.common.util.Constants.CategoryPats.ADMIN_CATEGORY_PATS;

@RestController
@RequestMapping(value = ADMIN_CATEGORY_PATS, produces = "application/json")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CompletableFuture<ResponseEntity<APIResultResponse<CategoryResponse>>> createNewCategoryAsync(
            @RequestBody @Valid CreateCategoryRequest request) {
        return categoryService.addCategory(request)
                .thenApplyAsync(categoryResponse -> {
                    APIResultResponse<CategoryResponse> responseDTO = new APIResultResponse<>(
                            HttpStatus.CREATED,
                            "Category successfully created",
                            categoryResponse
                    );
                    return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
                });
    }

    @PutMapping("/{slugCategory}")
    public CompletableFuture<ResponseEntity<APIResponse>> updateCategory(@PathVariable String slugCategory, @RequestBody @Valid UpdateCategoryRequest request) {
        return categoryService.updateCategory(slugCategory, request)
                .thenApplyAsync(aVoid -> {
                    APIResponse responseDTO =  new APIResponse(
                            HttpStatus.OK,
                            "Category successfully updated"
                    );
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                });
    }

    @DeleteMapping("/{slugCategory}")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable String slugCategory) {
        categoryService.deleteCategory(slugCategory);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Category successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResultResponse<Page<CategoryResponse>>> getCategory(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<CategoryResponse> categoryResponses = categoryService.getAllCategory(pageable);
        APIResultResponse<Page<CategoryResponse>> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Category successfully retrieved",
                categoryResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{slugCategory}")
    public ResponseEntity<APIResultResponse<CategoryResponse>> getCategoryDetail(@PathVariable String slugCategory) {
        CategoryResponse categoryResponse = categoryService.getCategoryDetail(slugCategory);
        APIResultResponse<CategoryResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Category successfully retrieved",
                categoryResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
