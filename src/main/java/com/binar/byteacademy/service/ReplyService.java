package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateReplyRequest;
import com.binar.byteacademy.dto.request.UpdateReplyRequest;
import com.binar.byteacademy.dto.response.CommentResponse;
import com.binar.byteacademy.dto.response.ReplyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.UUID;

public interface ReplyService {
    ReplyResponse addReply(CreateReplyRequest request, Principal connectedUser);
    ReplyResponse getReplyDetail(UUID id);
    Page<ReplyResponse> getAllReplyByComment(Pageable pageable, UUID idComment);
    ReplyResponse updateReply(UUID id, UpdateReplyRequest request, Principal connectedUser);
    void deleteReply(UUID id, Principal connectedUser);
}
