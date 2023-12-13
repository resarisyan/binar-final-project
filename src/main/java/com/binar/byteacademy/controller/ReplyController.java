package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.request.ReplyRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import com.binar.byteacademy.dto.response.ReplyResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.entity.Reply;
import com.binar.byteacademy.service.ReplyService;
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

import static com.binar.byteacademy.common.util.Constants.ReplyPats.REPLY_PATS;

@RestController
@RequestMapping(value = REPLY_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Reply", description = "Reply API")
public class ReplyController {
    private final ReplyService replyService;


    @GetMapping()
    @Schema(name = "GetReplyList", description = "Get reply list")
    @Operation(summary = "Endpoint to handle get reply list")
    public ResponseEntity<APIResultResponse<Page<ReplyResponse>>> getReplyList(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 9);
        Page<ReplyResponse> replyResponsePage = replyService.getReplyList(pageable);
        APIResultResponse<Page<ReplyResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success get discussion list",
                replyResponsePage
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Schema(name = "GetReplyById", description = "Get reply by id")
    @Operation(summary = "Endpoint to handle get reply by id")
    public ResponseEntity<APIResultResponse<ReplyResponse>> getReplyById(@PathVariable UUID id) {
        ReplyResponse replyResponse = replyService.getReplyById(id);
        APIResultResponse<ReplyResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success get reply by id",
                replyResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/create-reply")
    @Schema(name = "SaveReply", description = "Save reply")
    @Operation(summary = "Endpoint to handle save reply")
    public ResponseEntity<APIResultResponse<ReplyResponse>> saveReply(@RequestBody ReplyRequest replyRequest,@RequestParam("discussionTopic") String discussionTopic) {
        ReplyResponse replyResponse = replyService.saveReply(replyRequest, discussionTopic );
        APIResultResponse<ReplyResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Success Create Reply",
                replyResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update-reply/{id}")
    @Schema(name = "UpdateReply", description = "Update reply")
    @Operation(summary = "Endpoint to handle update reply")
    public ResponseEntity<APIResultResponse<ReplyResponse>> updateReply(@RequestBody ReplyRequest replyRequest, @PathVariable UUID id) {
        ReplyResponse replyResponse = replyService.updateReply(replyRequest, id);
        APIResultResponse<ReplyResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success update reply",
                replyResponse
        );
          return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Schema(name = "DeleteReply", description = "Delete reply")
    @Operation(summary = "Endpoint to handle delete reply")
    public ResponseEntity<APIResultResponse> deleteReplyById(@PathVariable UUID id) {
        replyService.deleteReplyById(id);
        APIResultResponse<ReplyResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success delete reply",
                null
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


}
