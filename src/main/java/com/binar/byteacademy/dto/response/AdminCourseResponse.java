package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumStatus;
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
public class AdminCourseResponse implements Serializable {
    private String slugCourse;
    private String courseName;
    private CategoryResponse category;
    private Integer price;
    private EnumCourseType courseType;
    private EnumCourseLevel courseLevel;
    private EnumStatus courseStatus;
}