package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.request.UpdatePaymentProofRequest;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.service.PaymentProofService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.binar.byteacademy.common.util.Constants.PaymentProofPats.PAYMENT_PROOF_PATS;

@RestController
@RequestMapping(value = PAYMENT_PROOF_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Payment Proof", description = "Payment Proof API")
public class PaymentProofController {
    private final PaymentProofService paymentProofService;

    @PutMapping("/{slugCourse}")
    @Schema(name = "PaymentProofRequest", description = "Payment proof request body")
    @Operation(summary = "Endpoint to handle payment proof")
    public ResponseEntity<APIResponse> updatePaymentProof(@PathVariable String slugCourse, @RequestBody @Valid UpdatePaymentProofRequest request) {
        paymentProofService.updatePaymentProof(slugCourse, request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Payment Proof status successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
