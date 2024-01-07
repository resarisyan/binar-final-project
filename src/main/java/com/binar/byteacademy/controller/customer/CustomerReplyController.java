package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.request.CreateReplyRequest;
import com.binar.byteacademy.dto.request.UpdateReplyRequest;
import com.binar.byteacademy.dto.response.ReplyResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.ReplyService;
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

import static com.binar.byteacademy.common.util.Constants.ReplyPats.CUSTOMER_REPLY_PATS;

@RestController
@RequestMapping(value = CUSTOMER_REPLY_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer Reply", description = "Customer Reply API")
public class CustomerReplyController {
    private final ReplyService replyService;

    @PostMapping
    @Schema(name = "AddReply", description = "Add reply")
    @Operation(summary = "Endpoint to handle add reply (User Role : Customer)")
    public ResponseEntity<APIResultResponse<ReplyResponse>> createNewReply(
            @RequestBody @Valid CreateReplyRequest request, Principal connectedUser) {
        ReplyResponse replyResponse = replyService.addReply(request, connectedUser);
        APIResultResponse<ReplyResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Reply successfully created",
                replyResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    @GetMapping("/comment/{idComment}")
    @Schema(name = "GetAllReplyByComment", description = "Get all reply by comment")
    @Operation(summary = "Endpoint to handle get all reply by comment (User Role : Customer)")
    public ResponseEntity<APIResultResponse<Page<ReplyResponse>>> getAllReplyByComment(@RequestParam("page") int page, @PathVariable UUID idComment) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<ReplyResponse> replyResponses = replyService.getAllReplyByComment(pageable, idComment);
        APIResultResponse<Page<ReplyResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Reply successfully retrieved",
                replyResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{idReply}")
    @Schema(name = "GetReplyDetail", description = "Get reply detail")
    @Operation(summary = "Endpoint to handle get reply detail (User Role : Customer)")
    public ResponseEntity<APIResultResponse<ReplyResponse>> getReplyDetail(@PathVariable UUID idReply) {
        ReplyResponse replyResponse = replyService.getReplyDetail(idReply);
        APIResultResponse<ReplyResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Reply successfully retrieved",
                replyResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{idReply}")
    @Schema(name = "UpdateReply", description = "Update reply")
    @Operation(summary = "Endpoint to handle update reply (User Role : Customer)")
    public ResponseEntity<APIResponse> updateReply(@PathVariable UUID idReply, @RequestBody @Valid UpdateReplyRequest request, Principal connectedUser) {
        replyService.updateReply(idReply, request, connectedUser);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Reply successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{idReply}")
    @Schema(name = "DeleteReply", description = "Delete reply")
    @Operation(summary = "Endpoint to handle delete reply (User Role : Customer)")
    public ResponseEntity<APIResponse> deleteReply(@PathVariable UUID idReply, Principal connectedUser) {
        replyService.deleteReply(idReply, connectedUser);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Reply successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
