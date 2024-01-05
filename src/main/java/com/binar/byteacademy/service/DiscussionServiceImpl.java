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
import org.springframework.cache.annotation.*;
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
@CacheConfig(cacheNames = "discussions")
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final CourseRepository courseRepository;
    private final SlugUtil slugUtil;

    @Override
    @Cacheable(key = "'getDiscussionDetail-' + #slug", unless = "#result == null")
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
    @CacheEvict(value = "allDiscussion", allEntries = true, condition = "#result != null")
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
    @Cacheable(value = "allDiscussion", key = "'getDiscussionByCourse-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #slugCourse", unless = "#result == null")
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
    @CachePut(key = "'getDiscussionDetail-' + #slug", unless = "#result == null")
    @CacheEvict(value = "allDiscussion", allEntries = true, condition = "#result != null")
    public DiscussionResponse updateDiscussion(String slug, DiscussionRequest request) {
        try {
            return discussionRepository.findBySlugDiscussion(slug)
                    .map(discussion -> {
                        discussion.setDiscussionContent(request.getDiscussionContent());
                        discussion.setDiscussionTopic(request.getDiscussionTopic());
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
                    .orElseThrow(() -> new DataNotFoundException(DISCUSSION_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error update discussion");
        }
    }

    @Override
    @Cacheable(value = "allDiscussion", key = "'getAllDiscussion-' + #pageable.pageNumber + '-' + #pageable.pageSize", unless = "#result == null")
    public Page<DiscussionResponse> getAllDiscussion(Pageable pageable) {
        try {
            Page<Discussion> discussionPage = Optional.of(discussionRepository.findAll(pageable))
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
            throw new ServiceBusinessException("Error get all discussion");
        }
    }

    @Override
    @CachePut(key = "'getDiscussionDetail-' + #slug", unless = "#result == null")
    @CacheEvict(value = "allDiscussion", allEntries = true, condition = "#result != null")
    public DiscussionResponse updateStatusDiscussion(String slug) {
        try {
            return discussionRepository.findBySlugDiscussion(slug)
                    .map(discussion -> {
                        discussion.setComplete(!discussion.isComplete());
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
                    .orElseThrow(() -> new DataNotFoundException(DISCUSSION_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Error update status discussion");
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'getDiscussionDetail-' + #slug"),
            @CacheEvict(value = {"allDiscussion", "allComments", "comments", "replies", "allReplies"}, allEntries = true)
    })
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