package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CreateChapterRequest;
import com.binar.byteacademy.dto.request.UpdateChapterRequest;
import com.binar.byteacademy.dto.response.ChapterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChapterService {
    ChapterResponse addChapter(CreateChapterRequest request);
    void updateChapter(String slug, UpdateChapterRequest request);
    void deleteChapter(String slug);
    Page<ChapterResponse> getAllChapter(Pageable pageable);
    ChapterResponse getChapterDetail(String slug);
    List<ChapterResponse> getListChapterBySlugCourse(String slugCourse);
}
