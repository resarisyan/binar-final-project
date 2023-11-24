package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.request.LoginRequest;
import com.binar.byteacademy.dto.request.RegisterRequest;
import com.binar.byteacademy.dto.response.LoginResponse;
import com.binar.byteacademy.dto.response.RefreshTokenResponse;
import com.binar.byteacademy.dto.response.RegisterResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.AuthenticationService;
import com.binar.byteacademy.service.OtpService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.binar.byteacademy.common.util.Constants.AuthPats.AUTH_PATS;

@RestController
@RequestMapping(value = AUTH_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final OtpService otpService;

    @PostMapping("/register")
    @Schema(name = "RegisterRequest", description = "Register request body")
    @Operation(summary = "Endpoint to handle register")
    public ResponseEntity<APIResultResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse customerResponse = authenticationService.register(request);
        APIResultResponse<RegisterResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Customer successfully created",
                customerResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    @Hidden
    public ResponseEntity<APIResultResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authenticationService.login(request);
        APIResultResponse<LoginResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Login success",
                loginResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/verify-account")
    @Schema(name = "VerifyAccountRequest", description = "Verify account request body")
    @Operation(summary = "Endpoint to handle verify account")
    public ResponseEntity<APIResponse> verifyAccount(@RequestParam String username,
                                                     @RequestParam String otp) {
        otpService.verifyAccount(otp, username);
        APIResponse response =  new APIResponse(
                HttpStatus.OK,
                "Account successfully verified"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/regenerate-otp")
    @Schema(name = "RegenerateOtpRequest", description = "Regenerate OTP request body")
    @Operation(summary = "Endpoint to handle regenerate OTP")
    public ResponseEntity<APIResponse> regenerateOtp(@RequestParam String username) {
        otpService.generateOtp(username, null);
        APIResponse response =  new APIResponse(
                HttpStatus.OK,
                "OTP successfully regenerated"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    @Schema(name = "RefreshTokenRequest", description = "Refresh token request body")
    @Operation(summary = "Endpoint to handle refresh token")
    public ResponseEntity<APIResultResponse<RefreshTokenResponse>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(request, response);
        APIResultResponse<RefreshTokenResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Refresh token success",
                refreshTokenResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}

