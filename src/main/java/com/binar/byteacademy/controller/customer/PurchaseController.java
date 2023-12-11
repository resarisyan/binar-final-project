package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.request.purchase.BankTransferPurchaseRequest;
import com.binar.byteacademy.dto.request.purchase.CreditCardPurchaseRequest;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.dto.response.PurchaseResponse;
import com.binar.byteacademy.dto.response.PurchaseDetailResponse;
import com.binar.byteacademy.service.PurchaseService;
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

import static com.binar.byteacademy.common.util.Constants.PurchasePats.PURCHASE_PATS;

@RestController
@RequestMapping(value = PURCHASE_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Purchase", description = "Purchase API")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping("/bank-transfer")
    @Schema(name = "BankTransferPurchaseRequest", description = "Bank transfer purchase request body")
    @Operation(summary = "Endpoint to handle bank transfer purchase")
    public ResponseEntity<APIResultResponse<PurchaseResponse>> makeBankTransferPurchase(@RequestBody @Valid BankTransferPurchaseRequest request) {
        PurchaseResponse purchaseResponse = purchaseService.makeBankTransferPurchase(request);
        APIResultResponse<PurchaseResponse> responseDTO = new APIResultResponse<>(
          HttpStatus.CREATED,
          "Purchase successfully created",
                purchaseResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/credit-card")
    @Schema(name = "BankTransferPurchaseRequest", description = "Bank transfer purchase request body")
    @Operation(summary = "Endpoint to handle bank transfer purchase")
    public ResponseEntity<APIResultResponse<PurchaseResponse>> makeCreditCardPurchase(@RequestBody @Valid CreditCardPurchaseRequest request) {
        PurchaseResponse purchaseResponse = purchaseService.makeCreditCardPurchase(request);
        APIResultResponse<PurchaseResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Purchase successfully created",
                purchaseResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/transaction-history")
    @Schema(name = "GetTransactionHistory", description = "Get transaction history request body")
    @Operation(summary = "Endpoint to get transaction history")
    public ResponseEntity<APIResultResponse<Page<PurchaseDetailResponse>>> getTransactionHistory(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<PurchaseDetailResponse> purchaseDetailResponses = purchaseService.getAllPurchaseDetailsForCustomer(pageable);
        APIResultResponse<Page<PurchaseDetailResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "transaction history successfully retrieved",
                purchaseDetailResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
