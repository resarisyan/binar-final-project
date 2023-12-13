package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.entity.Discussion;
import com.binar.byteacademy.service.DiscussionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.DiscussionPats.DISCUSSION_PATS;

@RestController
@RequestMapping(value = DISCUSSION_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Discussion", description = "Discussion API")
public class DiscussionController {

    private final DiscussionService discussionService;

    @GetMapping()
    @Schema(name = "GetDiscussionList", description = "Get discussion list")
    @Operation(summary = "Endpoint to handle get discussion list")
    public ResponseEntity<APIResultResponse<Page<DiscussionResponse>>> getDiscussionList(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 9);
        Page<DiscussionResponse> discussionResponsePage = discussionService.getListDiscussion(pageable);
        APIResultResponse<Page<DiscussionResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success get discussion list",
                discussionResponsePage
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Schema(name = "GetDiscussionById", description = "Get discussion by id")
    @Operation(summary = "Endpoint to handle get discussion by id")
    public ResponseEntity<APIResultResponse<DiscussionResponse>> getDiscussionById(@PathVariable UUID id) {
        DiscussionResponse discussionResponse = discussionService.getDiscussionById(id);
        APIResultResponse<DiscussionResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success get discussion by id",
                discussionResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/create-discussion")
    @Schema(name = "SaveDiscussion", description = "Save discussion")
    @Operation(summary = "Endpoint to handle save discussion")
    public ResponseEntity<APIResultResponse<DiscussionResponse>> saveDiscussion(@RequestBody DiscussionRequest discussionRequest) {
        DiscussionResponse discussionResponse = discussionService.saveDiscussion(discussionRequest);
        APIResultResponse<DiscussionResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Success save discussion",
                discussionResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update-discussion/{id}")
    @Schema(name = "UpdateDiscussion", description = "Update discussion")
    @Operation(summary = "Endpoint to handle update discussion")
    public ResponseEntity<APIResultResponse<DiscussionResponse>> updateDiscussion(@RequestBody DiscussionRequest discussionRequest, @PathVariable UUID id) {
        DiscussionResponse discussionResponse = discussionService.updateDiscussion(discussionRequest, id);
        APIResultResponse<DiscussionResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success update discussion",
                discussionResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Schema(name = "DeleteDiscussionById", description = "Delete discussion by id")
    @Operation(summary = "Endpoint to handle delete discussion by id")
    public ResponseEntity<APIResultResponse> deleteDiscussionById(@PathVariable UUID id) {
        discussionService.deleteDiscussionById(id);
        APIResultResponse responseDTO = new APIResultResponse<>(
                HttpStatus.NO_CONTENT,
                "Success delete discussion by id",
                null
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
