package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CourseRatingRequest;
import com.binar.byteacademy.dto.response.CourseRatingResponse;
import com.binar.byteacademy.entity.CourseRating;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CourseRatingRepository;
import com.binar.byteacademy.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CourseRatingServiceImpl implements CourseRatingService{
    private final CourseRatingRepository courseRatingRepository;
    private final UserProgressRepository userProgressRepository;
    @Override
    public CourseRatingResponse addCourseRating(CourseRatingRequest request, Principal connectedUser) {
        try{
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            return userProgressRepository.findByCourse_SlugCourseAndUserAndIsCompleted(request.getSlugCourse(), user, true)
                    .map(userProgress -> {
                        CourseRating courseRating = CourseRating.builder()
                                .rating(request.getRating())
                                .comment(request.getComment())
                                .user(user)
                                .course(userProgress.getCourse())
                                .build();
                        courseRatingRepository.save(courseRating);
                        return CourseRatingResponse.builder()
                                .rating(courseRating.getRating())
                                .comment(courseRating.getComment())
                                .courseName(courseRating.getCourse().getCourseName())
                                .build();
                    })
                    .orElseThrow(() -> new DataNotFoundException("Course not found or not completed")
                    );
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error add course rating");
        }
    }
}
