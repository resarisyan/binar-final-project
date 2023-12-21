package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.response.PurchaseResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.binar.byteacademy.common.util.Constants.PurchasePats.ADMIN_PURCHASE_PATS;

@RestController
@RequestMapping(value = ADMIN_PURCHASE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Purchase", description = "Admin Purchase API")
public class AdminPurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping
    @Schema(name = "PurchaseResponse", description = "Get all purchase response body")
    @Operation(summary = "Endpoint to handle get all purchase (User Role : Admin)")
    public ResponseEntity<APIResultResponse<Page<PurchaseResponse>>> getAllPurchase(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<PurchaseResponse> purchaseResponses = purchaseService.getAllPurchaseDetailsForAdmin(pageable);
        APIResultResponse<Page<PurchaseResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Purchase successfully retrieved",
                purchaseResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
