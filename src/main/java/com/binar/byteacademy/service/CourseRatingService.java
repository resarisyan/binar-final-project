package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CourseRatingRequest;
import com.binar.byteacademy.dto.response.CourseRatingResponse;

import java.security.Principal;

public interface CourseRatingService {
   CourseRatingResponse addCourseRating(CourseRatingRequest request, Principal connectedUser);
}
