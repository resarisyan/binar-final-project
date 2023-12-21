package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.response.PurchaseResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.PurchaseService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

import static com.binar.byteacademy.common.util.Constants.PurchasePats.CUSTOMER_PURCHASE_PATS;

@RestController
@RequestMapping(value = CUSTOMER_PURCHASE_PATS, produces = "application/json")
@RequiredArgsConstructor
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

    @GetMapping
    public ResponseEntity<APIResultResponse<Page<PurchaseResponse>>> getAllPurchase(@RequestParam("page") int page, Principal connectedUser) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<PurchaseResponse> purchaseResponses = purchaseService.getAllPurchaseDetailsForCustomer(pageable, connectedUser);
        APIResultResponse<Page<PurchaseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Purchase successfully retrieved",
                purchaseResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    @Hidden
    public ResponseEntity<APIResponse> paymentCallback(@RequestBody Map<String, Object> request) {
        purchaseService.paymentCallback(request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Payment successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
