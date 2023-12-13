package com.binar.byteacademy.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscussionResponse {
    private UUID id;
    private String courseName;
    private String discussionTopic;
    private String discussionContent;
    private LocalDateTime discussionDate;
    private boolean isComplete;
    private String username;
}
