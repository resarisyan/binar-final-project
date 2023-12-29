package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse implements Serializable {
    private String courseName;
    private String instructorName;
    private Integer price;
    private Double totalCourseRate;
    private Integer totalChapters;
    private Integer courseDuration;
    private String slugCourse;
    private EnumCourseType courseType;
    private EnumCourseLevel courseLevel;
    private String pathCourseImage;
    private CategoryResponse category;
    private String targetMarket;
}