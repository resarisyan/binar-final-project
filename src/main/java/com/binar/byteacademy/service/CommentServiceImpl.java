package com.binar.byteacademy.service;


import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.dto.request.CommentRequest;
import com.binar.byteacademy.dto.request.UpdateCommentRequest;
import com.binar.byteacademy.dto.response.CommentResponse;
import com.binar.byteacademy.entity.Comment;
import com.binar.byteacademy.entity.Discussion;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CommentRepository;
import com.binar.byteacademy.repository.DiscussionRepository;
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
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final DiscussionRepository discussionRepository;
    private final JwtUtil jwtUtil;



    @Override
    public CommentResponse getCommentById(UUID id) {
        try {
            User user = jwtUtil.getUser();
            return commentRepository.findById(id)
                    .map(comment -> CommentResponse.builder()
                            .commentContent(comment.getCommentContent())
                            .username(user.getUsername())
                            .commentDate(comment.getCreatedAt())
                            .build())
                    .orElseThrow(() -> new DataNotFoundException("Comment Not Found"));
        } catch (DataNotFoundException e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public CommentResponse saveComment(CommentRequest commentRequest) {
        try {
            User user = jwtUtil.getUser();
            Discussion discussion = discussionRepository.findBySlugDiscussion(commentRequest.getSlugDiscussion());
            Comment comment = commentRepository.save(Comment.builder()
                    .commentContent(commentRequest.getCommentContent())
                    .discussion(discussion)
                    .user(user)
                    .build());
            return CommentResponse.builder()
                    .commentContent(comment.getCommentContent())
                    .username(user.getUsername())
                    .commentDate(comment.getCreatedAt())
                    .build();
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public Page<CommentResponse> getListComment(Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            return Optional.of(commentRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .map(commentPage -> commentPage
                            .map(comment -> CommentResponse.builder()
                                    .commentContent(comment.getCommentContent())
                                    .username(user.getUsername())
                                    .commentDate(comment.getCreatedAt())
                                    .build()))
                    .orElseThrow(() -> new DataNotFoundException("Comment Not Found"));
        } catch (DataNotFoundException e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public CommentResponse updateComment(UpdateCommentRequest request, UUID id) {
        try {
            User user = jwtUtil.getUser();
            Optional<Comment> commentOptional = Optional.of(commentRepository.findById(id))
                    .orElseThrow(() -> new DataNotFoundException("Comment Not Found"));
            if (commentOptional.isPresent()) {
                Comment comment = commentOptional.get();
                comment.setCommentContent(request.getCommentContent());
                comment.setUpdatedAt(LocalDateTime.now());
                commentRepository.save(comment);
                return CommentResponse.builder()
                        .commentContent(comment.getCommentContent())
                        .username(user.getUsername())
                        .commentDate(comment.getCreatedAt())
                        .build();
            } else {
                throw new DataNotFoundException("Comment Not Found");
            }
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public void deleteCommentById(UUID id) {
        try{
         Optional<Comment> comment = commentRepository.findById(id);
         if(comment.isPresent()){
             commentRepository.deleteById(id);
         }else{
             throw new DataNotFoundException("Comment Not Found");
         }
        }catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}
