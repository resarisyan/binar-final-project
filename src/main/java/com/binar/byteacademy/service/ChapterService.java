package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ChapterRequest;
import com.binar.byteacademy.dto.response.CategoryResponse;
import com.binar.byteacademy.dto.response.ChapterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChapterService {
    ChapterResponse addChapter(ChapterRequest request);
    void updateChapter(UUID id, ChapterRequest request);
    void deleteChapter(UUID id);
    Page<ChapterResponse> getAllChapter(Pageable pageable);
    ChapterResponse getChapterDetail(UUID id);
}
