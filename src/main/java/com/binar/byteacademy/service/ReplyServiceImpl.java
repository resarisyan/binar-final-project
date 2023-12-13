package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.dto.request.ReplyRequest;
import com.binar.byteacademy.dto.response.ReplyResponse;
import com.binar.byteacademy.entity.Reply;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.DiscussionRepository;
import com.binar.byteacademy.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final DiscussionRepository discussionRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Page<ReplyResponse> getReplyList(Pageable pageable) {
        try {
            return Optional.of(replyRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .map(replyPage -> replyPage
                            .map(reply -> ReplyResponse.builder()
                                    .discussionTopic(reply.getDiscussion().getDiscussionTopic())
                                    .replyContent(reply.getReplyContent())
                                    .replyDate(reply.getReplyDate())
                                    .username(reply.getUser().getUsername())
                                    .build()))
            .orElseThrow(() -> new DataNotFoundException("Reply Not Found"));
        } catch (DataNotFoundException e) {
            log.error("No reply found");
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all reply", e);
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public ReplyResponse saveReply(ReplyRequest request, String discussionTopic) {
        try {
            User user = jwtUtil.getUser();
            return Optional.of(discussionRepository.findByDiscussionTopic(discussionTopic))
                    .map(discussion -> {
                        Reply reply = Reply.builder()
                                .discussion(discussion)
                                .replyContent(request.getReplyContent())
                                .replyDate(LocalDateTime.now())
                                .user(user)
                                .build();
                        replyRepository.save(reply);
                        return ReplyResponse.builder()
                                .discussionTopic(reply.getDiscussion().getDiscussionTopic())
                                .replyContent(reply.getReplyContent())
                                .replyDate(reply.getReplyDate())
                                .username(reply.getUser().getUsername())
                                .build();
                    }).orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));
        } catch (Exception e) {
            log.error("Failed to create reply", e);
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public ReplyResponse getReplyById(UUID replyId) {
        try {
            return Optional.of(replyRepository.findById(replyId))
                    .map(reply -> ReplyResponse.builder()
                            .discussionTopic(reply.get().getDiscussion().getDiscussionTopic())
                            .replyContent(reply.get().getReplyContent())
                            .replyDate(reply.get().getReplyDate())
                            .username(reply.get().getUser().getUsername())
                            .build())
                    .orElseThrow(() -> new DataNotFoundException("Reply Not Found"));
        } catch (DataNotFoundException e) {
            log.error("No reply found");
            throw e;
        }
    }

    @Override
    public void deleteReplyById(UUID replyId) {
        try {
            Optional<Reply> replyOptional = Optional.of(replyRepository.findById(replyId)).orElseThrow(() -> new DataNotFoundException("Reply Not Found"));
            if (replyOptional.isPresent()) {
                replyRepository.deleteById(replyId);
            } else {
                throw new DataNotFoundException("Reply Not Found");
            }
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete reply");
        }
    }

    @Override
    public ReplyResponse updateReply(ReplyRequest replyRequest, UUID replyId) {
        try {
            User user = jwtUtil.getUser();
            Optional<Reply> replyOptional = Optional.of(replyRepository.findById(replyId))
                    .orElseThrow(() -> new DataNotFoundException("Reply Not Found"));

            if (replyOptional.isPresent()) {
                Reply reply = replyOptional.get();
                reply.setReplyContent(replyRequest.getReplyContent());
                replyRepository.save(reply);
                return ReplyResponse.builder()
                        .discussionTopic(reply.getDiscussion().getDiscussionTopic())
                        .replyContent(reply.getReplyContent())
                        .replyDate(reply.getReplyDate())
                        .username(user.getUsername())
                        .build();
            }
        } catch (Exception e) {
            log.error("Failed to update reply", e);
            throw new ServiceBusinessException(e.getMessage());
        }
        return null;
    }
}
