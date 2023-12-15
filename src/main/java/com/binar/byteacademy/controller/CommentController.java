package com.binar.byteacademy.controller;


import com.binar.byteacademy.dto.request.CommentRequest;
import com.binar.byteacademy.dto.request.UpdateCommentRequest;
import com.binar.byteacademy.dto.response.CommentResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.CommentService;
import com.binar.byteacademy.service.CommentServiceImpl;
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

import static com.binar.byteacademy.common.util.Constants.CommentPats.COMMENT_PATS;

@RestController
@RequestMapping(value = COMMENT_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping()
    @Schema(name = "GetCommentList", description = "Get Comment list")
    @Operation(summary = "Endpoint to handle get comment list")
    public ResponseEntity<APIResultResponse<Page<CommentResponse>>> getCommentList(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 9);
        Page<CommentResponse> commentResponsePage = commentService.getListComment(pageable);
        APIResultResponse<Page<CommentResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success get comment list",
                commentResponsePage
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Schema(name = "GetCommentById", description = "Get comment by id")
    @Operation(summary = "Endpoint to handle get comment by id")
    public ResponseEntity<APIResultResponse<CommentResponse>> getCommentById(@RequestParam("id") UUID id) {
        CommentResponse commentResponse = commentService.getCommentById(id);
        APIResultResponse<CommentResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success get comment by id",
                commentResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/create-comment")
    @Schema(name = "SaveComment", description = "Save comment")
    @Operation(summary = "Endpoint to handle save comment")
    public ResponseEntity<APIResultResponse<CommentResponse>> saveComment(@RequestBody CommentRequest request) {
        CommentResponse commentResponse = commentService.saveComment(request);
        APIResultResponse<CommentResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success save comment",
                commentResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update-comment/{id}")
    @Schema(name = "UpdateComment", description = "Update comment")
    @Operation(summary = "Endpoint to handle update comment")
    public ResponseEntity<APIResultResponse<CommentResponse>> updateComment(@RequestBody UpdateCommentRequest request, @PathVariable UUID id) {
        CommentResponse commentResponse = commentService.updateComment(request,id);
        APIResultResponse<CommentResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success update comment",
                commentResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Schema(name = "DeleteComment", description = "Delete comment")
    @Operation(summary = "Endpoint to handle delete comment")
    public ResponseEntity<APIResultResponse<CommentResponse>> deleteCommentById(@PathVariable UUID id) {
        commentService.deleteCommentById(id);
        APIResultResponse<CommentResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Success delete comment",
                null
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
