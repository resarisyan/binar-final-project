package com.binar.byteacademy.dto.request;

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
public class UpdateChapterRequest {
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
    @NotBlank
    @ValidSlug
    private String slugChapter;
}
