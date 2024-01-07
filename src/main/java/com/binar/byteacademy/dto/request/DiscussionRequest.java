package com.binar.byteacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionRequest {
    @NotBlank
    private String discussionTopic;

    @NotBlank
    private String discussionContent;

    @NotBlank
    private String slugCourse;

}
