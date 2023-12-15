package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CommentRequest;
import com.binar.byteacademy.dto.request.UpdateCommentRequest;
import com.binar.byteacademy.dto.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CommentService {
    CommentResponse getCommentById(UUID id);

    CommentResponse saveComment(CommentRequest commentRequest);

    Page<CommentResponse> getListComment(Pageable pageable);

    CommentResponse updateComment(UpdateCommentRequest request, UUID id);

    void deleteCommentById(UUID id);

}
