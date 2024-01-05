package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.request.CreateCommentRequest;
import com.binar.byteacademy.dto.request.UpdateCommentRequest;
import com.binar.byteacademy.dto.response.CommentResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.CommentPats.CUSTOMER_COMMENT_PATS;

@RestController
@RequestMapping(value = CUSTOMER_COMMENT_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer Comment", description = "Customer Comment API")
public class CustomerCommentController {
    private final CommentService commentService;

    @PostMapping
    @Schema(name = "AddComment", description = "Add comment")
    @Operation(summary = "Endpoint to handle add comment (User Role : Customer)")
    public ResponseEntity<APIResultResponse<CommentResponse>> addComment(
            @RequestBody @Valid CreateCommentRequest request,
            Principal connectedUser) {
        CommentResponse commentResponse = commentService.addComment(request, connectedUser);
        APIResultResponse<CommentResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Comment successfully added",
                commentResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/discussion/{slugDiscussion}")
    @Schema(name = "GetAllCommentByDiscussion", description = "Get all comment by discussion")
    @Operation(summary = "Endpoint to handle get all comment by discussion (User Role : Customer)")
    public ResponseEntity<APIResultResponse<Page<CommentResponse>>> getAllCommentByDiscussion(@RequestParam("page") int page, @PathVariable String slugDiscussion) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<CommentResponse> commentResponse = commentService.getAllCommentByDiscussion(pageable, slugDiscussion);
        APIResultResponse<Page<CommentResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Comment successfully retrieved",
                commentResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Schema(name = "GetCommentDetail", description = "Get comment detail")
    @Operation(summary = "Endpoint to handle get comment detail (User Role : Customer)")
    public ResponseEntity<APIResultResponse<CommentResponse>> getCommentDetail(@PathVariable UUID id) {
        CommentResponse commentResponse = commentService.getCommentDetail(id);
        APIResultResponse<CommentResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Comment successfully retrieved",
                commentResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Schema(name = "DeleteComment", description = "Delete comment")
    @Operation(summary = "Endpoint to handle delete comment (User Role : Customer)")
    public ResponseEntity<APIResponse> deleteComment(@PathVariable UUID id, Principal connectedUser) {
        commentService.deleteCommentByIdAndUser(id, connectedUser);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Comment successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Schema(name = "UpdateComment", description = "Update comment")
    @Operation(summary = "Endpoint to handle update comment (User Role : Customer)")
    public ResponseEntity<APIResponse> updateComment(
            @PathVariable UUID id,
            @RequestBody UpdateCommentRequest request,
            Principal connectedUser) {
        commentService.updateComment(id, request, connectedUser);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Comment successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
