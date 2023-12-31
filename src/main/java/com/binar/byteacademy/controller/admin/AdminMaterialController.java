package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.MaterialRequest;
import com.binar.byteacademy.dto.response.MaterialResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.MaterialService;
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

import static com.binar.byteacademy.common.util.Constants.MaterialPats.ADMIN_MATERIAL_PATS;

@RestController
@RequestMapping(value = ADMIN_MATERIAL_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Material", description = "Admin Material API")
public class AdminMaterialController {
    private final MaterialService materialService;

    @PostMapping
    @Schema(name = "MaterialRequest", description = "Create material request body")
    @Operation(summary = "Endpoint to handle create new material (User Role : Admin)")
    public ResponseEntity<APIResultResponse<MaterialResponse>> createNewChapter(
            @RequestBody @Valid MaterialRequest request) {
        MaterialResponse materialResponse = materialService.addMaterial(request);
        APIResultResponse<MaterialResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Material successfully created",
                materialResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{slugMaterial}")
    @Schema(name = "MaterialRequest", description = "Update material request body")
    @Operation(summary = "Endpoint to handle update material (User Role : Admin)")
    public ResponseEntity<APIResponse> updateMaterial(@PathVariable String slugMaterial, @RequestBody @Valid MaterialRequest request) {
        materialService.updateMaterial(slugMaterial, request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Material successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{slugMaterial}")
    @Schema(name = "MaterialRequest", description = "Delete material request body")
    @Operation(summary = "Endpoint to handle delete material (User Role : Admin)")
    public ResponseEntity<APIResponse> deleteMaterial(@PathVariable String slugMaterial) {
        materialService.deleteMaterial(slugMaterial);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Material successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    @Schema(name = "MaterialRequest", description = "Get material request body")
    @Operation(summary = "Endpoint to handle get material (User Role : Admin)")
    public ResponseEntity<APIResultResponse<Page<MaterialResponse>>> getCategory(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<MaterialResponse> materialResponses = materialService.getAllMaterial(pageable);
        APIResultResponse<Page<MaterialResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Material successfully retrieved",
                materialResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{slugMaterial}")
    @Schema(name = "MaterialRequest", description = "Get material request body")
    @Operation(summary = "Endpoint to handle get material (User Role : Admin)")
    public ResponseEntity<APIResultResponse<MaterialResponse>> getMaterial(@PathVariable String slugMaterial) {
        MaterialResponse materialResponse = materialService.getMaterialDetailAdmin(slugMaterial);
        APIResultResponse<MaterialResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Material successfully retrieved",
                materialResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
