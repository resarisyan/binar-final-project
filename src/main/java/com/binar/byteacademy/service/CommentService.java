package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateCommentRequest;
import com.binar.byteacademy.dto.request.UpdateCommentRequest;
import com.binar.byteacademy.dto.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.UUID;

public interface CommentService {
    CommentResponse getCommentDetail(UUID id);

    CommentResponse addComment(CreateCommentRequest request, Principal connectedUser);

    Page<CommentResponse> getAllCommentByDiscussion(Pageable pageable, String slugDiscussion);

    CommentResponse updateComment(UUID id, UpdateCommentRequest request,  Principal connectedUser);

    void deleteComment(UUID id);

    void deleteCommentByIdAndUser(UUID id, Principal connectedUser);
}