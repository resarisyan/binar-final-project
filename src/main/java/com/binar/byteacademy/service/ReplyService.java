package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateReplyRequest;
import com.binar.byteacademy.dto.request.UpdateReplyRequest;
import com.binar.byteacademy.dto.response.ReplyResponse;

import java.security.Principal;
import java.util.UUID;

public interface ReplyService {
    ReplyResponse addReply(CreateReplyRequest request, Principal connectedUser);
    ReplyResponse getReplyDetail(UUID id);
    void updateReply(UUID id, UpdateReplyRequest request, Principal connectedUser);
    void deleteReply(UUID id, Principal connectedUser);
}
