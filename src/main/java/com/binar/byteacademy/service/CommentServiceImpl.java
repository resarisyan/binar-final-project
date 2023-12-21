package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateCommentRequest;
import com.binar.byteacademy.dto.request.UpdateCommentRequest;
import com.binar.byteacademy.dto.response.CommentResponse;
import com.binar.byteacademy.entity.Comment;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CommentRepository;
import com.binar.byteacademy.repository.DiscussionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COMMENT_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.DISCUSSION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final DiscussionRepository discussionRepository;

    @Override
    public CommentResponse getCommentDetail(UUID id) {
        try {
            return commentRepository.findById(id)
                    .map(comment -> CommentResponse.builder()
                            .commentContent(comment.getCommentContent())
                            .username(comment.getUser().getUsername())
                            .commentDate(comment.getCreatedAt())
                            .build())
                    .orElseThrow(() -> new DataNotFoundException(COMMENT_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public CommentResponse addComment(CreateCommentRequest request, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try {
            return discussionRepository.findBySlugDiscussion(request.getSlugDiscussion())
                    .map(discussion -> {
                        Comment comment = Comment.builder()
                                .commentContent(request.getCommentContent())
                                .discussion(discussion)
                                .user(user)
                                .build();
                        commentRepository.save(comment);
                        return CommentResponse.builder()
                                .commentContent(comment.getCommentContent())
                                .username(user.getUsername())
                                .commentDate(comment.getCreatedAt())
                                .build();
                    })
                    .orElseThrow(() -> new DataNotFoundException(DISCUSSION_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error add comment");
        }
    }

    @Override
    public Page<CommentResponse> getAllCommentByDiscussion(Pageable pageable, String slugDiscussion) {
        try {
            Page<Comment> commentPage = Optional.ofNullable(commentRepository.findAllByDiscussion_SlugDiscussion(pageable, slugDiscussion))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(COMMENT_NOT_FOUND));
            return commentPage.map(comment -> CommentResponse.builder()
                    .commentContent(comment.getCommentContent())
                    .username(comment.getUser().getUsername())
                    .commentDate(comment.getCreatedAt())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error get all comment by discussion");
        }
    }

    @Override
    public void updateComment(UUID id, UpdateCommentRequest request, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            commentRepository.findByIdAndUser(id, user)
                    .ifPresentOrElse(commentOptional -> {
                        commentOptional.setCommentContent(request.getCommentContent());
                        commentRepository.save(commentOptional);
                    }, () -> {
                        throw new DataNotFoundException(COMMENT_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error update comment");
        }
    }

    @Override
    public void deleteCommentByIdAndUser(UUID id, Principal connectedUser) {
        try{
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            commentRepository.findByIdAndUser(id, user)
                    .ifPresentOrElse(commentRepository::delete, () -> {
                        throw new DataNotFoundException(COMMENT_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error delete comment");
        }
    }

    @Override
    public void deleteComment(UUID id) {
        try {
            commentRepository.findById(id)
                    .ifPresentOrElse(commentRepository::delete, () -> {
                        throw new DataNotFoundException(COMMENT_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error delete comment");
        }
    }
}