package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.UpdatePurchaseStatusRequest;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.dto.response.AdminPurchaseDetailResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.binar.byteacademy.common.util.Constants.PurchasePats.ADMIN_PURCHASE_PATS;

@RestController
@RequestMapping(value = ADMIN_PURCHASE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Purchase Admin", description = "Purchase API Admin")
public class AdminPurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping
    @Schema(name = "GetAllTransactionHistory", description = "Get all transaction history request body")
    @Operation(summary = "Endpoint to get all transaction history")
    public ResponseEntity<APIResultResponse<Page<AdminPurchaseDetailResponse>>> getAllTransactionHistory(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<AdminPurchaseDetailResponse> purchaseDetailResponses = purchaseService.getAllPurchaseDetailsForAdmin(pageable);
        APIResultResponse<Page<AdminPurchaseDetailResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "All transaction history successfully retrieved",
                purchaseDetailResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/status/{slugPurchase}")
    @Schema(name = "UpdatePurchaseStatusRequest", description = "Update purchase status request body")
    @Operation(summary = "Endpoint to handle update purchase status (User Role : Admin)")
    public CompletableFuture<ResponseEntity<APIResponse>> updatePurchaseStatus(@PathVariable String slugPurchase, @RequestBody UpdatePurchaseStatusRequest request) {
        return purchaseService.updatePurchaseStatus(request, slugPurchase)
                .thenApplyAsync(purchaseResponse -> {
                    APIResponse responseDTO = new APIResponse(
                            HttpStatus.OK,
                            "Purchase status successfully updated"
                    );
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                });
    }
}
