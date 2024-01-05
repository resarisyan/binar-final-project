package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.DiscussionService;
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

import static com.binar.byteacademy.common.util.Constants.DiscussionPats.ADMIN_DISCUSSION_PATS;

@RestController
@RequestMapping(value = ADMIN_DISCUSSION_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Discussion", description = "Admin Discussion API")
public class AdminDiscussionController {
    private final DiscussionService discussionService;

    @GetMapping
    @Schema(name = "GetAllDiscussion", description = "Get all discussion")
    @Operation(summary = "Endpoint to handle get all discussion (User Role : Admin)")
    public ResponseEntity<APIResultResponse<Page<DiscussionResponse>>> getAllDiscussion(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<DiscussionResponse> discussionResponses = discussionService.getAllDiscussion(pageable);
        APIResultResponse<Page<DiscussionResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Discussion successfully retrieved",
                discussionResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{slugDiscussion}")
    @Schema(name = "GetDiscussionDetail", description = "Get discussion detail")
    @Operation(summary = "Endpoint to handle get discussion detail (User Role : Admin)")
    public ResponseEntity<APIResultResponse<DiscussionResponse>> getDiscussionDetail(@PathVariable String slugDiscussion) {
        DiscussionResponse discussionResponse = discussionService.getDiscussionDetail(slugDiscussion);
        APIResultResponse<DiscussionResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Discussion successfully retrieved",
                discussionResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{slugDiscussion}")
    @Schema(name = "UpdateDiscussion", description = "Update discussion")
    @Operation(summary = "Endpoint to handle update discussion (User Role : Admin)")
    public ResponseEntity<APIResponse> updateDiscussion(@PathVariable String slugDiscussion, @RequestBody @Valid DiscussionRequest request) {
        discussionService.updateDiscussion(slugDiscussion, request);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Discussion successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{slugDiscussion}")
    @Schema(name = "DeleteDiscussion", description = "Delete discussion")
    @Operation(summary = "Endpoint to handle delete discussion (User Role : Admin)")
    public ResponseEntity<APIResponse> deleteDiscussion(@PathVariable String slugDiscussion) {
        discussionService.deleteDiscussion(slugDiscussion);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Discussion successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{slugDiscussion}/status")
    @Schema(name = "UpdateStatusDiscussion", description = "Update status discussion")
    @Operation(summary = "Endpoint to handle update status discussion (User Role : Admin)")
    public ResponseEntity<APIResponse> updateStatusDiscussion(@PathVariable String slugDiscussion) {
        discussionService.updateStatusDiscussion(slugDiscussion);
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "Discussion successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
