package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.SlugUtil;
import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import com.binar.byteacademy.entity.Discussion;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.DiscussionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.binar.byteacademy.common.util.Constants.TableName.DISCUSSION_TABLE;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.DISCUSSION_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COURSE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final CourseRepository courseRepository;
    private final SlugUtil slugUtil;

    @Override
    public DiscussionResponse getDiscussionDetail(String slug) {
        try {
            return discussionRepository.findBySlugDiscussion(slug)
                    .map(discussion -> DiscussionResponse.builder()
                            .discussionContent(discussion.getDiscussionContent())
                            .discussionDate(discussion.getCreatedAt())
                            .username(discussion.getUser().getUsername())
                            .build())
                    .orElseThrow(() -> new DataNotFoundException(DISCUSSION_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error get discussion detail");
        }
    }

    @Override
    public DiscussionResponse addDiscussion(DiscussionRequest request, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            String slug = slugUtil.toSlug(DISCUSSION_TABLE, "slug_discussion", request.getDiscussionTopic());

            return courseRepository.findFirstBySlugCourse(request.getSlugCourse())
                    .map(course -> {
                        Discussion discussion = Discussion.builder()
                                .discussionContent(request.getDiscussionContent())
                                .discussionTopic(request.getDiscussionTopic())
                                .discussionDate(LocalDateTime.now())
                                .isComplete(false)
                                .slugDiscussion(slug)
                                .course(course)
                                .user(user)
                                .build();
                        discussionRepository.save(discussion);
                        return DiscussionResponse.builder()
                                .discussionContent(discussion.getDiscussionContent())
                                .discussionDate(discussion.getCreatedAt())
                                .username(discussion.getUser().getUsername())
                                .discussionDate(discussion.getDiscussionDate())
                                .slugDiscussion(discussion.getSlugDiscussion())
                                .discussionTopic(discussion.getDiscussionTopic())
                                .isComplete(discussion.isComplete())
                                .courseName(discussion.getCourse().getCourseName())
                                .build();
                    })
                    .orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error add discussion");
        }
    }

    @Override
    public Page<DiscussionResponse> getDiscussionByCourse(Pageable pageable, String slugCourse) {
        try {
            Page<Discussion> discussionPage = Optional.ofNullable(discussionRepository.findAllByCourse_SlugCourse(pageable, slugCourse))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(DISCUSSION_NOT_FOUND));
            return discussionPage.map(discussion -> DiscussionResponse.builder()
                    .discussionContent(discussion.getDiscussionContent())
                    .discussionDate(discussion.getCreatedAt())
                    .username(discussion.getUser().getUsername())
                    .discussionDate(discussion.getDiscussionDate())
                    .slugDiscussion(discussion.getSlugDiscussion())
                    .discussionTopic(discussion.getDiscussionTopic())
                    .isComplete(discussion.isComplete())
                    .courseName(discussion.getCourse().getCourseName())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error get discussion by course");
        }
    }

    @Override
    public void updateDiscussion(String slug, DiscussionRequest request, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            discussionRepository.findBySlugDiscussionAndUser(slug, user)
                    .ifPresentOrElse(discussion -> {
                        discussion.setDiscussionContent(request.getDiscussionContent());
                        discussion.setDiscussionTopic(request.getDiscussionTopic());
                        discussionRepository.save(discussion);
                    }, () -> {
                        throw new DataNotFoundException(DISCUSSION_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error update discussion");
        }
    }

    @Override
    public void updateStatusDiscussion(String slug) {
        try {
            discussionRepository.findBySlugDiscussion(slug)
                    .ifPresentOrElse(discussion -> {
                        discussion.setComplete(!discussion.isComplete());
                        discussionRepository.save(discussion);
                    }, () -> {
                        throw new DataNotFoundException(DISCUSSION_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error update status discussion");
        }
    }

    @Override
    public void deleteDiscussion(String slug) {
        try {
            discussionRepository.findBySlugDiscussion(slug)
                    .ifPresentOrElse(discussionRepository::delete, () -> {
                        throw new DataNotFoundException(DISCUSSION_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error delete discussion");
        }
    }
}