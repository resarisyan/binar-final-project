package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface DiscussionService {
    DiscussionResponse getDiscussionDetail(String slug);
    DiscussionResponse addDiscussion(DiscussionRequest request, Principal connectedUser);
    Page<DiscussionResponse> getDiscussionByCourse(Pageable pageable, String slugCourse);
    void updateDiscussion(String slug, DiscussionRequest request, Principal connectedUser);
    void updateStatusDiscussion(String slug);
    void deleteDiscussion(String slug);
}