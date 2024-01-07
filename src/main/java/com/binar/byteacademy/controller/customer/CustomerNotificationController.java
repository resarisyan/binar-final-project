package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.response.ReceiverNotificationResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.NotificationService;
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

import java.security.Principal;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.NotificationPats.CUSTOMER_NOTIFICATION_PATS;

@RestController
@RequestMapping(value = CUSTOMER_NOTIFICATION_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer Notification", description = "Customer Notification API")
public class CustomerNotificationController {
    private final NotificationService notificationService;
    @GetMapping
    @Schema(name = "ReceiverNotificationResponse", description = "Receiver notification response body")
    @Operation(summary = "Endpoint to handle get all notification (User Role : Customer)")
    public ResponseEntity<APIResultResponse<Page<ReceiverNotificationResponse>>> getAllNotification(@RequestParam("page") int page, Principal connectedUser) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<ReceiverNotificationResponse> receiverNotificationResponses = notificationService.getAllNotification(pageable, connectedUser);
        APIResultResponse<Page<ReceiverNotificationResponse>> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Notification successfully retrieved",
                receiverNotificationResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/count")
    @Schema(name = "ReceiverNotificationResponse", description = "Receiver notification response body")
    @Operation(summary = "Endpoint to handle get all notification (User Role : Customer)")
    public ResponseEntity<APIResultResponse<Integer>> countUnreadNotification(Principal connectedUser) {
        int count = notificationService.countUnreadNotification(connectedUser);
        APIResultResponse<Integer> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Notification successfully retrieved",
                count
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/read")
    @Schema(name = "UpdateNotificationResponse", description = "Update notification response body")
    @Operation(summary = "Endpoint to handle update notification (User Role : Customer)")
    public ResponseEntity<APIResultResponse<ReceiverNotificationResponse>> readNotification(@PathVariable("id") UUID id, Principal connectedUser) {
        ReceiverNotificationResponse receiverNotificationResponse = notificationService.readNotification(id, connectedUser);
        APIResultResponse<ReceiverNotificationResponse> responseDTO =  new APIResultResponse<>(
                HttpStatus.OK,
                "Notification successfully updated",
                receiverNotificationResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
