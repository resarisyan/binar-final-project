package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterRequest {
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
}
