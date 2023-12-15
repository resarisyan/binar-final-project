package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.request.UpdateDiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.Discussion;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.DiscussionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.binar.byteacademy.common.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final CourseRepository courseRepository;
    private final JwtUtil jwtUtil;

    @Override
    public DiscussionResponse getDiscussionById(UUID id) {
        try {
            return Optional.of(discussionRepository.findById(id))
                    .map(discussion -> DiscussionResponse.builder()
                            .courseName(discussion.get().getCourse().getCourseName())
                            .discussionTopic(discussion.get().getDiscussionTopic())
                            .discussionContent(discussion.get().getDiscussionContent())
                            .discussionDate(discussion.get().getDiscussionDate())
                            .slugDiscussion(discussion.get().getSlugDiscussion())
                            .isComplete(discussion.get().isComplete())
                            .username(discussion.get().getUser().getUsername())
                            .build())
                    .orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));
        } catch (DataNotFoundException e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public DiscussionResponse saveDiscussion(DiscussionRequest discussionRequest) {
        try {
            User user = jwtUtil.getUser();

            return courseRepository.findFirstBySlugCourse(discussionRequest.getSlugCourse())
                    .map(course1 -> {
                        Discussion discussion = Discussion.builder()
                                .course(course1)
                                .slugDiscussion(discussionRequest.getSlugDiscussion())
                                .discussionContent(discussionRequest.getDiscussionContent())
                                .discussionTopic(discussionRequest.getDiscussionTopic())
                                .discussionDate(LocalDateTime.now())
                                .isComplete(false)
                                .user(user)
                                .build();
                        discussionRepository.save(discussion);
                        return DiscussionResponse.builder()
                                .courseName(discussion.getCourse().getCourseName())
                                .discussionTopic(discussion.getDiscussionTopic())
                                .discussionContent(discussion.getDiscussionContent())
                                .discussionDate(discussion.getDiscussionDate())
                                .slugDiscussion(discussion.getSlugDiscussion())
                                .isComplete(discussion.isComplete())
                                .username(discussion.getUser().getUsername())
                                .build();
                    })
                    .orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public Page<DiscussionResponse> getListDiscussion(Pageable pageable) {
        try {
           return Optional.of(discussionRepository.findAll(pageable))
                   .filter(Page::hasContent)
                    .map(discussionPage -> discussionPage
                            .map(discussion -> DiscussionResponse.builder()
                                    .courseName(discussion.getCourse().getCourseName())
                                    .discussionTopic(discussion.getDiscussionTopic())
                                    .discussionContent(discussion.getDiscussionContent())
                                    .slugDiscussion(discussion.getSlugDiscussion())
                                    .discussionDate(LocalDateTime.now())
                                    .username(discussion.getUser().getUsername())
                                    .isComplete(false)
                                    .build()))
                    .orElseThrow(() -> new DataNotFoundException("Discussion Not found"));

        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("failed to get all discussion");
        }
    }

    @Override
    public DiscussionResponse updateDiscussion(UpdateDiscussionRequest request, UUID id) {
        try {
            User user = jwtUtil.getUser();
            Optional<Discussion> discussionOptional = Optional.of(discussionRepository.findById(id))
                    .orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));
            if (discussionOptional.isPresent()) {
                Discussion discussion = discussionOptional.get();
                discussion.setDiscussionContent(request.getDiscussionContent());
                discussion.setUpdatedAt(LocalDateTime.now());
                discussionRepository.save(discussion);
                return DiscussionResponse.builder()
                        .courseName(discussion.getCourse().getCourseName())
                        .discussionTopic(discussion.getDiscussionTopic())
                        .discussionContent(discussion.getDiscussionContent())
                        .discussionDate(discussion.getDiscussionDate())
                        .slugDiscussion(discussion.getSlugDiscussion())
                        .isComplete(discussion.isComplete())
                        .username(user.getUsername())
                        .build();
            }
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
        return null;
    }


    @Override
    public void deleteDiscussionById(UUID id) {
        try {
            Optional<Discussion> discussionOptional = Optional.of(discussionRepository.findById(id))
                    .orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));
            if(discussionOptional.isPresent()){
                discussionRepository.deleteById(id);
            }else {
                throw new DataNotFoundException("Discussion Not Found");
            }
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete discussion");
        }
    }


}
