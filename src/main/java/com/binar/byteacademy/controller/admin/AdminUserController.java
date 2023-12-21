package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.response.UserAdminResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.binar.byteacademy.common.util.Constants.UserPats.ADMIN_USER_PATS;

@RestController
@RequestMapping(value = ADMIN_USER_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin User", description = "Admin User API")
public class AdminUserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<APIResultResponse<UserAdminResponse>> getCustomerDetail(Principal connectedUser) {
        UserAdminResponse userCustomerResponse = userService.getAdminDetail(connectedUser);
        APIResultResponse<UserAdminResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Customer detail successfully retrieved",
                userCustomerResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
