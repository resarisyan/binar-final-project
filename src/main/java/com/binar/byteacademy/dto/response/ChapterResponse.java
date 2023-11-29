package com.binar.byteacademy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ChapterResponse {
    private UUID chapterId;
    private Integer noChapter;
    private String title;
    private Integer chapterDuration;
    private List<MaterialResponse> materials;
}
