package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ReplyRequest;
import com.binar.byteacademy.dto.response.ReplyResponse;
import com.binar.byteacademy.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReplyService {

    Page<ReplyResponse> getReplyList(Pageable pageable);

    ReplyResponse saveReply(ReplyRequest replyRequest,String discussionTopic);
    ReplyResponse getReplyById(UUID replyId);
    
    void deleteReplyById(UUID replyId);
    ReplyResponse updateReply(ReplyRequest replyRequest, UUID reply);
}
