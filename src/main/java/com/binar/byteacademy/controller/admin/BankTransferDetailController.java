package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.CreateBankTransferDetailRequest;
import com.binar.byteacademy.dto.request.UpdateBankTransferDetailRequest;
import com.binar.byteacademy.dto.response.BankTransferDetailResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.BankTransferDetailService;
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

import static com.binar.byteacademy.common.util.Constants.BankTransferDetailPats.BANK_TRANSFER_DETAIL_PATS;

@RestController
@RequestMapping(value = BANK_TRANSFER_DETAIL_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Bank Transfer Detail", description = "Bank Transfer Detail API")
public class BankTransferDetailController {
    private final BankTransferDetailService bankTransferDetailService;

    @PostMapping("/")
    @Schema(name = "CreateBankTransferDetail", description = "Create bank transfer detail request body")
    @Operation(summary = "Endpoint to handle create new bank transfer detail")
    public ResponseEntity<APIResultResponse<BankTransferDetailResponse>> createNewBankTransferDetail(@RequestBody @Valid CreateBankTransferDetailRequest request) {
        BankTransferDetailResponse bankTransferPurchaseResponse = bankTransferDetailService.addNewBankTransferDetail(request);
        APIResultResponse<BankTransferDetailResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Bank transfer detail successfully created",
                bankTransferPurchaseResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{bankName}")
    @Schema(name = "UpdateBankTransferDetail", description = "Update bank transfer detail request body")
    @Operation(summary = "Endpoint to handle update bank transfer detail")
    public ResponseEntity<APIResponse> updateBankTransferDetail(@PathVariable String bankName, @RequestBody @Valid UpdateBankTransferDetailRequest request) {
        bankTransferDetailService.updateBankTransferDetail(bankName, request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Bank transfer detail successfully updates"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{bankName}")
    @Schema(name = "DeleteBankTransferDetail", description = "Delete bank transfer detail request body")
    @Operation(summary = "Endpoint to handle delete bank transfer detail")
    public ResponseEntity<APIResponse> deleteBankTransferDetail(@PathVariable String bankName) {
        bankTransferDetailService.deleteBankTransferDetail(bankName);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Bank transfer detail successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{bankName}")
    @Schema(name = "GetBankTransferDetail", description = "Get bank transfer detail request body")
    @Operation(summary = "Endpoint to handle get bank transfer detail")
    public ResponseEntity<APIResultResponse<BankTransferDetailResponse>> getBankTransferDetail(@PathVariable String bankName) {
        BankTransferDetailResponse bankTransferDetailResponse = bankTransferDetailService.getBankTransferDetail(bankName);
        APIResultResponse<BankTransferDetailResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Bank transfer detail successfully retrieved",
                bankTransferDetailResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    @Schema(name = "GetAllBankTransferDetail", description = "Get all bank transfer detail request body")
    @Operation(summary = "Endpoint to handle get all bank transfer detail")
    public ResponseEntity<APIResultResponse<Page<BankTransferDetailResponse>>> getAllBankTransferDetail(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<BankTransferDetailResponse> bankTransferDetailResponses = bankTransferDetailService.getAllBankTransferDetail(pageable);
        APIResultResponse<Page<BankTransferDetailResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Bank transfer detail successfully retrieved",
                bankTransferDetailResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
