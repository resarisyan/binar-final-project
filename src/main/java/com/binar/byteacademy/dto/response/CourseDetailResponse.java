package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.entity.Category;
import com.binar.byteacademy.entity.Chapter;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailResponse {
    private UUID id;
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
    private List<ChapterResponse> listChapter;
    private CategoryResponse category;
}
