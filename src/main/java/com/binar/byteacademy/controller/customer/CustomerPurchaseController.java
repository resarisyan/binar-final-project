package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.response.PurchaseResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.PurchaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

import static com.binar.byteacademy.common.util.Constants.CustomerPurchasePats.CUSTOMER_PURCHASE_PATS;

@RestController
@RequestMapping(value = CUSTOMER_PURCHASE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Purchase", description = "Customer Purchase API")
public class CustomerPurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping("/{slug-course}")
    public ResponseEntity<APIResultResponse<PurchaseResponse>> purchaseCourse(@PathVariable("slug-course") String slugCourse, Principal connectedUser) {
        PurchaseResponse purchaseResponse = purchaseService.createPurchase(slugCourse, connectedUser);
        APIResultResponse<PurchaseResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Purchase successfully created",
                purchaseResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse> paymentCallback(@RequestBody Map<String, Object> request) {
        purchaseService.paymentCallback(request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Payment successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
