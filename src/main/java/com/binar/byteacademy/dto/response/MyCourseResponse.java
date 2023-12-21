package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyCourseResponse {
    private String courseName;
    private String instructorName;
    private Double totalCourseRate;
    private Integer totalModules;
    private Integer courseDuration;
    private String slugCourse;
    private EnumCourseLevel courseLevel;
    private String pathCourseImage;
    private CategoryResponse category;
    private Integer coursePercentage;
    private UserProgressResponse userProgressResponse;
}
