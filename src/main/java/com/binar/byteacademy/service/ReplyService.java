package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ReplyRequest;
import com.binar.byteacademy.dto.request.UpdateReplyRequest;
import com.binar.byteacademy.dto.response.ReplyResponse;
import com.binar.byteacademy.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReplyService {

    Page<ReplyResponse> getReplyList(Pageable pageable);

    ReplyResponse saveReply(ReplyRequest replyRequest);
    ReplyResponse getReplyById(UUID id);
    
    void deleteReplyById(UUID id);
    ReplyResponse updateReply(UpdateReplyRequest request, UUID id);
}
