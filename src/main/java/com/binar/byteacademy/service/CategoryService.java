package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateCategoryRequest;
import com.binar.byteacademy.dto.request.UpdateCategoryRequest;
import com.binar.byteacademy.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.CompletableFuture;

public interface CategoryService {
    CompletableFuture<CategoryResponse> addCategory(CreateCategoryRequest request);
    CompletableFuture<Void> updateCategory(String slugCategory, UpdateCategoryRequest request);
    void deleteCategory(String slugCategory);
    Page<CategoryResponse> getAllCategory(Pageable pageable);
    CategoryResponse getCategoryDetail(String slugCategory);
}
