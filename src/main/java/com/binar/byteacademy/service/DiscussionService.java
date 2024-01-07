package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface DiscussionService {
    DiscussionResponse getDiscussionDetail(String slug);
    DiscussionResponse addDiscussion(DiscussionRequest request, Principal connectedUser);
    Page<DiscussionResponse> getAllDiscussion(Pageable pageable);
    Page<DiscussionResponse> getDiscussionByCourse(Pageable pageable, String slugCourse);
    DiscussionResponse updateDiscussion(String slug, DiscussionRequest request);
    DiscussionResponse updateStatusDiscussion(String slug);
    void deleteDiscussion(String slug);
}