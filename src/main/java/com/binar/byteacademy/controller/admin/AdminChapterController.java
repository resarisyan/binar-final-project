package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.ChapterRequest;
import com.binar.byteacademy.dto.response.ChapterResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ChapterPats.ADMIN_CHAPTER_PATS;

@RestController
@RequestMapping(value = ADMIN_CHAPTER_PATS, produces = "application/json")
@RequiredArgsConstructor
public class AdminChapterController {
    private final ChapterService chapterService;

    @PostMapping
    public ResponseEntity<APIResultResponse<ChapterResponse>> createNewChapter(
            @RequestBody @Valid ChapterRequest request) {
        ChapterResponse chapterResponse = chapterService.addChapter(request);
        APIResultResponse<ChapterResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Chapter successfully created",
                chapterResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateChapter(@PathVariable UUID id, @RequestBody @Valid ChapterRequest request) {
        chapterService.updateChapter(id, request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Chapter successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteChapter(@PathVariable UUID id) {
        chapterService.deleteChapter(id);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Chapter successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResultResponse<Page<ChapterResponse>>> getCategory(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<ChapterResponse> chapterResponses = chapterService.getAllChapter(pageable);
        APIResultResponse<Page<ChapterResponse>> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Chapter successfully retrieved",
                chapterResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResultResponse<ChapterResponse>> getChapterDetail(@PathVariable UUID id) {
        ChapterResponse chapterResponse = chapterService.getChapterDetail(id);
        APIResultResponse<ChapterResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Chapter successfully retrieved",
                chapterResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
