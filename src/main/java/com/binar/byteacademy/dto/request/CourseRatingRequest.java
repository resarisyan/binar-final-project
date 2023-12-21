package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRatingRequest {
    @NotBlank
    private String slugCourse;
    @NotNull
    @Digits(integer = 1, fraction = 0)
    @Min(1)
    @Max(5)
    private int rating;
    @NotBlank
    private String comment;
}
