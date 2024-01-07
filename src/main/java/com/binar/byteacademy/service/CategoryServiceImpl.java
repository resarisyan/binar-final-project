package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.common.util.ImageUtil;
import com.binar.byteacademy.common.util.SlugUtil;
import com.binar.byteacademy.dto.request.CreateCategoryRequest;
import com.binar.byteacademy.dto.request.UpdateCategoryRequest;
import com.binar.byteacademy.dto.response.CategoryResponse;
import com.binar.byteacademy.entity.Category;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CATEGORY_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.TableName.CATEGORY_TABLE;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SlugUtil slugUtil;
    private final ImageUtil imageUtil;
    private final CheckDataUtil checkDataUtil;

    @Override
    @Async("asyncExecutor")
    @CacheEvict(value = "allCategories", allEntries = true, condition = "#result != null")
    public CompletableFuture<CategoryResponse> addCategory(CreateCategoryRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String slug = Optional.ofNullable(request.getSlugCategory())
                        .orElse(slugUtil.toSlug(CATEGORY_TABLE, "slug_category", request.getCategoryName()));
                String pathCategoryImage = imageUtil.base64UploadImage(request.getPathCategoryImage())
                        .join();

                Category category = Category.builder()
                        .categoryName(request.getCategoryName())
                        .pathCategoryImage(pathCategoryImage)
                        .slugCategory(slug)
                        .build();

                Category savedCategory = categoryRepository.save(category);

                return CategoryResponse.builder()
                        .categoryName(savedCategory.getCategoryName())
                        .pathCategoryImage(savedCategory.getPathCategoryImage())
                        .slugCategory(savedCategory.getSlugCategory())
                        .build();
            } catch (Exception e) {
                throw new ServiceBusinessException("Failed to create category");
            }
        }).exceptionally(throwable -> {
            throw new ServiceBusinessException(throwable.getMessage());
        });
    }

    @Override
    @Async("asyncExecutor")
    @Caching(evict = {
            @CacheEvict(value = {"getCategoryDetail"}, key = "'getCategoryDetail-' + #slugCategory"),
            @CacheEvict(value = {
                    "allCourses", "chapters", "allChapters", "materials", "allMaterials",
                    "allPurchases", "discussions", "allDiscussion",
                    "allCoursePromos", "coursePromos",
                    "allPromos", "promos", "allComments", "comments",
                    "replies", "allReplies", "dashboard", "allCategories"
    }, allEntries = true)
    })
    public CompletableFuture<CategoryResponse> updateCategory(String slugCategory, UpdateCategoryRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return categoryRepository.findBySlugCategory(slugCategory)
                        .map(category -> {
                            checkDataUtil.checkDataField(CATEGORY_TABLE, "category_name", request.getCategoryName(), "category_id", category.getId());
                            category.setCategoryName(request.getCategoryName());
                            checkDataUtil.checkDataField(CATEGORY_TABLE, "slug_category", request.getSlugCategory(), "category_id", category.getId());
                            category.setSlugCategory(request.getSlugCategory());

                            Optional.ofNullable(request.getPathCategoryImage())
                                    .ifPresent(image -> {
                                        imageUtil.deleteImage(category.getPathCategoryImage());
                                        String pathCategoryImage = imageUtil.base64UploadImage(request.getPathCategoryImage()).join();
                                        category.setPathCategoryImage(pathCategoryImage);
                                    });

                            categoryRepository.save(category);
                            return CategoryResponse.builder()
                                    .categoryName(category.getCategoryName())
                                    .pathCategoryImage(category.getPathCategoryImage())
                                    .slugCategory(category.getSlugCategory())
                                    .build();
                        })
                        .orElseThrow(() -> new DataNotFoundException(CATEGORY_NOT_FOUND));
            } catch (DataNotFoundException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceBusinessException(e.getMessage());
            }
        });
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = {"getCategoryDetail"}, key = "'getCategoryDetail-' + #slugCategory"),
            @CacheEvict(value = {
                    "allCourses", "chapters", "allChapters", "materials", "allMaterials",
                    "allPurchases", "discussions", "allDiscussion",
                    "allCoursePromos", "coursePromos",
                    "allPromos", "promos", "allComments", "comments",
                    "replies", "allReplies", "dashboard", "allCategories"
    }, allEntries = true)
    })
    public void deleteCategory(String slugCategory) {
        try {
            categoryRepository.findBySlugCategory(slugCategory)
                    .ifPresentOrElse(category -> {
                        imageUtil.deleteImage(category.getPathCategoryImage());
                        categoryRepository.delete(category);
                    }, () -> {
                        throw new DataNotFoundException(CATEGORY_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete category");
        }
    }


    @Override
    @Cacheable(value = "allCategories", key = "'getAllCategory-' + #pageable.pageNumber + '-' + #pageable.pageSize", unless = "#result == null")
    public Page<CategoryResponse> getAllCategory(Pageable pageable) {
        try {
            Page<Category> categoryPage = Optional.of(categoryRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(CATEGORY_NOT_FOUND));
            return categoryPage.map(category -> CategoryResponse.builder()
                    .categoryName(category.getCategoryName())
                    .pathCategoryImage(category.getPathCategoryImage())
                    .slugCategory(category.getSlugCategory())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get all category");
        }
    }

    @Override
    @Cacheable(key = "'getCategoryDetail-' + #slugCategory", unless = "#result == null")
    public CategoryResponse getCategoryDetail(String slugCategory) {
        try {
            Category category = categoryRepository.findBySlugCategory(slugCategory)
                    .orElseThrow(() -> new DataNotFoundException(CATEGORY_NOT_FOUND));
            return CategoryResponse.builder()
                    .categoryName(category.getCategoryName())
                    .pathCategoryImage(category.getPathCategoryImage())
                    .slugCategory(category.getSlugCategory())
                    .build();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get category detail");
        }
    }

    @Override
    @Cacheable(value = "allCategories", key = "'getListCategory'", unless = "#result == null")
    public List<CategoryResponse> getListCategory() {
        try {
            List<Category> categoryList = categoryRepository.findAll();
            return categoryList.stream().map(category -> CategoryResponse.builder()
                    .categoryName(category.getCategoryName())
                    .pathCategoryImage(category.getPathCategoryImage())
                    .slugCategory(category.getSlugCategory())
                    .build()).toList();
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get list category");
        }
    }
}
