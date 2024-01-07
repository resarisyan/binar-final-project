package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateReplyRequest;
import com.binar.byteacademy.dto.request.UpdateReplyRequest;
import com.binar.byteacademy.dto.response.ReplyResponse;
import com.binar.byteacademy.entity.Reply;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CommentRepository;
import com.binar.byteacademy.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COMMENT_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.REPLY_NOT_FOUND;
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "replies")
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    @Override
    @CacheEvict(value = "allReplies", allEntries = true, condition = "#result != null")
    public ReplyResponse addReply(CreateReplyRequest request, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            return commentRepository.findById(request.getIdComment())
                    .map(comment -> {
                        Reply reply = Reply.builder()
                                .replyContent(request.getReplyContent())
                                .user(user)
                                .comment(comment)
                                .build();
                        replyRepository.save(reply);
                        return ReplyResponse.builder()
                                .replyContent(reply.getReplyContent())
                                .username(reply.getUser().getUsername())
                                .replyDate(reply.getCreatedAt())
                                .build();
                    })
                    .orElseThrow(() -> new DataNotFoundException(COMMENT_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error add reply");
        }
    }

    @Override
    @Cacheable(key = "'reply-' + #id", unless = "#result == null")
    public ReplyResponse getReplyDetail(UUID id) {
        try {
            return replyRepository.findById(id)
                    .map(reply -> ReplyResponse.builder()
                            .replyContent(reply.getReplyContent())
                            .username(reply.getUser().getUsername())
                            .replyDate(reply.getCreatedAt())
                            .build())
                    .orElseThrow(() -> new DataNotFoundException(REPLY_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error get reply detail");
        }
    }

    @Override
    @Transactional
    @Cacheable(value = "allReplies", key = "'getAllReplyByComment-' + #idComment + '-' + #pageable.pageNumber", unless = "#result == null")
    public Page<ReplyResponse> getAllReplyByComment(Pageable pageable, UUID idComment) {
        try {
            Page<Reply> replyPage = Optional.ofNullable(replyRepository.findAllByComment_Id(pageable, idComment))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(COMMENT_NOT_FOUND));
            return replyPage.map(reply -> ReplyResponse.builder()
                    .id(reply.getId())
                    .replyContent(reply.getReplyContent())
                    .username(reply.getUser().getUsername())
                    .replyDate(reply.getCreatedAt())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "allReplies", allEntries = true, condition = "#result != null")
    @CachePut(key = "'reply-' + #id", unless = "#result == null")
    public ReplyResponse updateReply(UUID id, UpdateReplyRequest request, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try {
            return replyRepository.findByIdAndUser(id, user)
                    .map(reply -> {
                        reply.setReplyContent(request.getReplyContent());
                        replyRepository.save(reply);
                        return ReplyResponse.builder()
                                .replyContent(reply.getReplyContent())
                                .username(reply.getUser().getUsername())
                                .replyDate(reply.getCreatedAt())
                                .build();
                    }).orElseThrow(() -> new DataNotFoundException(REPLY_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error update reply");
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allReplies", allEntries = true, condition = "#result != null"),
            @CacheEvict(key = "'reply-' + #id", condition = "#result != null")
    })
    public void deleteReply(UUID id, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try {
            replyRepository.findByIdAndUser(id, user)
                    .ifPresentOrElse(replyRepository::delete, () -> {
                        throw new DataNotFoundException(REPLY_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error delete reply");
        }
    }
}
