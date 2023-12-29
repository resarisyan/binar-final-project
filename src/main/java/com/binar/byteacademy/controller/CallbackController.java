package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.service.PurchaseService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.binar.byteacademy.common.util.Constants.CallbackPats.CALLBACK_PATS;

@RestController
@RequestMapping(value = CALLBACK_PATS, produces = "application/json")
@RequiredArgsConstructor
@Hidden
public class CallbackController {
    private final PurchaseService purchaseService;
    @PostMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse> paymentCallback(@RequestBody Map<String, Object> request) {
        purchaseService.paymentCallback(request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Payment successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
