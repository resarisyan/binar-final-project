package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.response.ChatOpenAiResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.OpenAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.binar.byteacademy.common.util.Constants.ChatOpenAiPats.CUSTOMER_CHAT_OPEN_AI_PATS;

@RestController
@RequestMapping(value = CUSTOMER_CHAT_OPEN_AI_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer Chat Open Ai", description = "Customer Chat Open Ai API")
public class CustomerChatOpenAiController {
    private final OpenAiService openAiService;

    @PostMapping("/chat")
    @Schema(name = "Chat", description = "Chat")
    @Operation(summary = "Endpoint to handle chat (User Role : Customer)")
    public ResponseEntity<APIResultResponse<ChatOpenAiResponse>> chatOpenAi(@RequestBody String message){
        ChatOpenAiResponse chatOpenAiResponse = openAiService.chat(message);
        APIResultResponse<ChatOpenAiResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Chat successfully retrieved",
                chatOpenAiResponse
        );
        return ResponseEntity.ok(responseDTO);
    }
}
