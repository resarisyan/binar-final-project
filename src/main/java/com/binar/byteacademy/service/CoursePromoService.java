package com.binar.byteacademy.service;


import com.binar.byteacademy.dto.request.CoursePromoRequest;
import com.binar.byteacademy.dto.response.CoursePromoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CoursePromoService {
    CoursePromoResponse addCoursePromo(CoursePromoRequest request);
    void updateCoursePromo(UUID id, CoursePromoRequest request);
    void deleteCoursePromo(UUID id);
    Page<CoursePromoResponse> getAllCoursePromo(Pageable pageable);
    CoursePromoResponse getCoursePromoDetail(UUID id);
}
