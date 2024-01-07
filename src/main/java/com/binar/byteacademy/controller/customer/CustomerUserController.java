package com.binar.byteacademy.controller.customer;

import com.binar.byteacademy.dto.response.UserCustomerResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.binar.byteacademy.common.util.Constants.UserPats.CUSTOMER_USER_PATS;

@RestController
@RequestMapping(value = CUSTOMER_USER_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Customer User", description = "Customer User API")
public class CustomerUserController {
    private final UserService userService;

    @GetMapping("/me")
    @Schema(name = "GetCustomerDetail", description = "Get customer detail")
    @Operation(summary = "Endpoint to handle get customer detail (User Role : Customer)")
    public ResponseEntity<APIResultResponse<UserCustomerResponse>> getCustomerDetail(Principal connectedUser) {
        UserCustomerResponse userCustomerResponse = userService.getCustomerDetail(connectedUser);
        APIResultResponse<UserCustomerResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Customer detail successfully retrieved",
                userCustomerResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
