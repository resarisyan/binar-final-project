package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private String courseName;
    private String courseSubTitle;
    private String instructorName;
    private Double price;
    private EnumCourseType courseType;
    private EnumCourseLevel courseLevel;
    private Double totalCourseRate;
    private Integer totalModules;
    private Integer courseDuration;
}
