package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.NotificationRequest;
import com.binar.byteacademy.dto.response.NotificationResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import static com.binar.byteacademy.common.util.Constants.NotificationPats.ADMIN_NOTIFICATION_PATS;

@RestController
@RequestMapping(value = ADMIN_NOTIFICATION_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Notification", description = "Admin Notification API")
public class AdminNotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send-to-all")
    @Operation(summary = "Endpoint to handle send notification to all user")
    @Schema(name = "NotificationRequest", description = "Notification request body")
    public ResponseEntity<APIResultResponse<NotificationResponse>> sendToAll(
            @RequestBody @Valid NotificationRequest request) {
        NotificationResponse notificationResponse = notificationService.sendToAll(request);
        APIResultResponse<NotificationResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Notification successfully created",
                notificationResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/send-to-user/{username}")
    @Operation(summary = "Endpoint to handle send notification to user")
    @Schema(name = "NotificationRequest", description = "Notification request body")
    public ResponseEntity<APIResultResponse<NotificationResponse>> sendToUser(
            @RequestBody @Valid NotificationRequest request, @PathVariable String username) {
        NotificationResponse notificationResponse = notificationService.sendToUser(request, username);
        APIResultResponse<NotificationResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Notification successfully created",
                notificationResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
