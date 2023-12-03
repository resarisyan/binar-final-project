package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDetailResponse {
    private String courseName;
    private String instructorName;
    private Double totalCourseRate;
    private Integer totalModules;
    private Integer courseDuration;
    private String slugCourse;
    private String pathCourseImage;
    private String groupLink;
    private EnumCourseType courseType;
    private EnumCourseLevel courseLevel;
    private List<ChapterCourseDetailResponse> chapters;
    private CategoryResponse category;
}
