package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.response.MaterialResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.binar.byteacademy.common.util.Constants.MaterialPats.CUSTOMER_MATERIAL_PATS;

@RestController
@RequestMapping(value = CUSTOMER_MATERIAL_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer Material", description = "Customer Material API")
public class CustomerMaterialController {
    private final MaterialService materialService;
    @GetMapping("/{slugMaterial}")
    @Schema(name = "MaterialResponse", description = "Material response body")
    @Operation(summary = "Endpoint to handle get material detail (User Role : Customer)")
    public ResponseEntity<APIResultResponse<MaterialResponse>> getMaterialDetail(@PathVariable String slugMaterial, Principal principal) {
        MaterialResponse materialResponse = materialService.getMaterialDetailCustomer(slugMaterial, principal);
        APIResultResponse<MaterialResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Material successfully retrieved",
                materialResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/{slugMaterial}/complete")
    @Schema(name = "MaterialResponse", description = "Material response body")
    @Operation(summary = "Endpoint to handle complete material (User Role : Customer)")
    public ResponseEntity<APIResponse>  completeMaterial(@PathVariable String slugMaterial, Principal principal) {
        materialService.completeMaterial(slugMaterial, principal);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Material successfully completed"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
