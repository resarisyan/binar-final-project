package com.binar.byteacademy.service;


import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.request.UpdateDiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import com.binar.byteacademy.entity.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;


public interface DiscussionService {

    DiscussionResponse getDiscussionById(UUID id);
    DiscussionResponse saveDiscussion(DiscussionRequest discussionRequest);
    Page<DiscussionResponse> getListDiscussion(Pageable pageable);

    DiscussionResponse updateDiscussion(UpdateDiscussionRequest request, UUID id);

    void deleteDiscussionById(UUID id);
}
