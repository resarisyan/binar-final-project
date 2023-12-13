package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.validation.Base64Image;
import com.binar.byteacademy.validation.FieldExistence;
import com.binar.byteacademy.validation.ValidSlug;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest {
    @NotBlank
    private String categoryName;

    @Base64Image
    private String pathCategoryImage;

    @ValidSlug
    @FieldExistence(tableName = "categories", fieldName = "slug_category", shouldExist = false, message = "Slug category already exists")
    private String slugCategory;
}
