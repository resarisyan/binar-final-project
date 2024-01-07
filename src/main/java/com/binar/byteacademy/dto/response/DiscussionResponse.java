package com.binar.byteacademy.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscussionResponse implements Serializable {
    private String courseName;
    private String discussionTopic;
    private String discussionContent;
    private String slugDiscussion;
    private LocalDateTime discussionDate;
    private boolean isComplete;
    private String username;
}