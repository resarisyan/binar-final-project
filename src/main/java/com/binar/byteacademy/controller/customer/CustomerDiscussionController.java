package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.request.DiscussionRequest;
import com.binar.byteacademy.dto.response.DiscussionResponse;
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

import java.security.Principal;

import static com.binar.byteacademy.common.util.Constants.DiscussionPats.CUSTOMER_DISCUSSION_PATS;

@RestController
@RequestMapping(value = CUSTOMER_DISCUSSION_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer Discussion", description = "Customer Discussion API")
public class CustomerDiscussionController {
    private final DiscussionService discussionService;

    @PostMapping
    @Schema(name = "AddDiscussion", description = "Add discussion")
    @Operation(summary = "Endpoint to handle add discussion (User Role : Customer)")
    public ResponseEntity<APIResultResponse<DiscussionResponse>> createNewDiscussion(
            @RequestBody @Valid DiscussionRequest request, Principal connectedUser) {
        DiscussionResponse discussionResponse = discussionService.addDiscussion(request, connectedUser);
        APIResultResponse<DiscussionResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Discussion successfully created",
                discussionResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{slugDiscussion}")
    @Schema(name = "GetDiscussionDetail", description = "Get discussion detail")
    @Operation(summary = "Endpoint to handle get discussion detail (User Role : Customer)")
    public ResponseEntity<APIResultResponse<DiscussionResponse>> getDiscussionDetail(@PathVariable String slugDiscussion) {
        DiscussionResponse discussionResponse = discussionService.getDiscussionDetail(slugDiscussion);
        APIResultResponse<DiscussionResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Discussion successfully retrieved",
                discussionResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/course/{slugCourse}")
    @Schema(name = "GetDiscussionByCourse", description = "Get discussion by course")
    @Operation(summary = "Endpoint to handle get discussion by course (User Role : Customer)")
    public ResponseEntity<APIResultResponse<Page<DiscussionResponse>>> getDiscussionByCourse(@RequestParam("page") int page, @PathVariable String slugCourse) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<DiscussionResponse> discussionResponses = discussionService.getDiscussionByCourse(pageable, slugCourse);
        APIResultResponse<Page<DiscussionResponse>> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Discussion successfully retrieved",
                discussionResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
