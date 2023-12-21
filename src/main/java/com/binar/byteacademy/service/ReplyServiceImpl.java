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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COMMENT_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.REPLY_NOT_FOUND;
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    @Override
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
    public void updateReply(UUID id, UpdateReplyRequest request, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try {
            replyRepository.findByIdAndUser(id, user)
                    .ifPresentOrElse(reply -> {
                        reply.setReplyContent(request.getReplyContent());
                        replyRepository.save(reply);
                    }, () -> {
                        throw new DataNotFoundException(REPLY_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error update reply");
        }
    }

    @Override
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
