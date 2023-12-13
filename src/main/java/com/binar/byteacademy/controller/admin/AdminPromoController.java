package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.request.PromoRequest;
import com.binar.byteacademy.dto.response.PromoResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.PromoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.binar.byteacademy.common.util.Constants.PromoPats.ADMIN_PROMO_PATS;

@RestController
@RequestMapping(value = ADMIN_PROMO_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Course Promo", description = "Admin Course Promo API")
public class AdminPromoController {
    private final PromoService promoService;

    @PostMapping
    public ResponseEntity<APIResultResponse<PromoResponse>> createNewPromo(
            @RequestBody @Valid PromoRequest request) {
        PromoResponse promoResponse = promoService.addPromo(request);
        APIResultResponse<PromoResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Promo successfully created",
                promoResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{promoCode}")
    public ResponseEntity<APIResponse> updatePromo(@PathVariable String promoCode, @RequestBody @Valid PromoRequest request) {
        promoService.updatePromo(promoCode, request);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Promo successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{promoCode}")
    public ResponseEntity<APIResponse> deletePromo(@PathVariable String promoCode) {
        promoService.deletePromo(promoCode);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Promo successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResultResponse<Page<PromoResponse>>> getPromo(@RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<PromoResponse> promoResponses = promoService.getAllPromo(pageable);
        APIResultResponse<Page<PromoResponse>> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Promo successfully retrieved",
                promoResponses
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{promoCode}")
    public ResponseEntity<APIResultResponse<PromoResponse>> getPromoDetail(@PathVariable String promoCode) {
        PromoResponse promoResponse = promoService.getPromoDetail(promoCode);
        APIResultResponse<PromoResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Promo successfully retrieved",
                promoResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
