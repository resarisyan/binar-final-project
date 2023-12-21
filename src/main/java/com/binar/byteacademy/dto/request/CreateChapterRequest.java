package com.binar.byteacademy.dto.request;

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
public class CreateChapterRequest {
    @NotNull
    @Digits(integer = 2, fraction = 0)
    private Integer noChapter;
    @NotBlank
    private String title;
    @NotNull
    @Digits(integer = 2, fraction = 0)
    private Integer chapterDuration;
    @NotBlank
    private String slugCourse;
    @ValidSlug
    @FieldExistence(tableName = "chapters", fieldName = "slug_chapter", shouldExist = false, message = "Slug chapter already exists")
    private String slugChapter;
}
