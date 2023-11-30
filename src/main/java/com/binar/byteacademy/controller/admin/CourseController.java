package com.binar.byteacademy.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.binar.byteacademy.common.util.Constants.CoursePats.ADMIN_COURSE_PATS;

@RestController
@RequestMapping(value = ADMIN_COURSE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Course Admin", description = "Course API Admin")
public class CourseController {

}
