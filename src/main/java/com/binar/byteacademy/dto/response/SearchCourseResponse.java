package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
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
public class SearchCourseResponse {
    private String courseName;
    private String categoryName;
    private String instructorName;
    private String pathImage;
    private Double price;
    private EnumCourseType courseType;
    private EnumCourseLevel courseLevel;
    private Double totalCourseRate;
    private Integer totalModules;
    private Integer courseDuration;
}
