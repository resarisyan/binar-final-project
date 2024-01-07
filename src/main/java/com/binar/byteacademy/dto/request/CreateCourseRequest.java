package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.validation.Base64Image;
import com.binar.byteacademy.validation.FieldExistence;
import com.binar.byteacademy.validation.ValidSlug;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseRequest {
    @NotBlank
    @FieldExistence(tableName = "courses", fieldName = "course_name", shouldExist = false, message = "Course name already exists")
    private String courseName;
    @NotBlank
    private String instructorName;
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private Integer price;
    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Integer courseDuration;
    @NotBlank
    private String courseDescription;
    @NotBlank
    private String targetMarket;
    @ValidSlug
    @FieldExistence(tableName = "courses", fieldName = "slug_course", shouldExist = false, message = "Slug course already exists")
    private String slugCourse;
    @NotBlank
    @Base64Image
    private String pathCourseImage;
    @NotBlank
    private String groupLink;
    @NotNull
    private EnumCourseType courseType;
    @NotNull
    private EnumCourseLevel courseLevel;
    @NotNull
    private EnumStatus courseStatus;
    @NotBlank
    private String slugCategory;
}
