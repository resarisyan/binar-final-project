package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursePromoRequest {
    @NotBlank
    private String slugCourse;
    @NotBlank
    private String promoCode;
}
