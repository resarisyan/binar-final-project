package com.binar.byteacademy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CategoryResponse {
    private UUID categoryId;
    private String categoryName;
    private String pathCategoryImage;
}
