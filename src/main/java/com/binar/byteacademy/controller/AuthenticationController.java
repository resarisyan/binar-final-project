package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.request.*;
import com.binar.byteacademy.dto.response.LoginResponse;
import com.binar.byteacademy.dto.response.RefreshTokenResponse;
import com.binar.byteacademy.dto.response.RegisterResponse;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.enumeration.EnumEmailVerificationType;
import com.binar.byteacademy.enumeration.EnumOtpType;
import com.binar.byteacademy.service.AuthenticationService;
import com.binar.byteacademy.service.EmailVerificationService;
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
    private final EmailVerificationService emailVerificationService;

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

    @PostMapping("/verify-register-phone")
    @Schema(name = "VerifyRegisterPhoneRequest", description = "Verify register phone request body")
    @Operation(summary = "Endpoint to handle verify register phone")
    public ResponseEntity<APIResponse> verifyRegisterPhone(@RequestBody @Valid VerifyRegisterPhoneRequest request) {
        otpService.verifyRegisterPhoneNumber(request);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Account successfully verified"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generate-otp-register")
    @Schema(name = "GenerateOtpRegisterRequest", description = "Generate otp register request body")
    @Operation(summary = "Endpoint to handle generate otp register")
    public ResponseEntity<APIResponse> generateOtpRegister(@RequestBody @Valid GenerateOtpRegisterPhoneRequest request) {
        otpService.sendOtp(request.getPhoneNumber(), EnumOtpType.REGISTER);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "OTP Register successfully regenerated"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-register-email")
    @Schema(name = "VerifyRegisterEmailRequest", description = "Verify register email request body")
    @Operation(summary = "Endpoint to handle verify register email")
    public ResponseEntity<APIResponse> verifyRegisterEmail(@RequestBody @Valid EmailTokenRequest request) {
        emailVerificationService.verifyEmailTokenRegister(request.getToken());
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email successfully verified"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-forgot-password-email")
    @Schema(name = "VerifyForgotPasswordEmailRequest", description = "Verify forgot password email request body")
    @Operation(summary = "Endpoint to handle verify forgot password email")
    public ResponseEntity<APIResponse> verifyForgotPasswordEmail(@RequestBody @Valid ResetPasswordRequest request) {
        emailVerificationService.verifyEmailTokenForgotPassword(request);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Password successfully reset"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password-email")
    @Schema(name = "ForgotPasswordEmailRequest", description = "Forgot password email request body")
    @Operation(summary = "Endpoint to handle forgot password email")
    public ResponseEntity<APIResponse> forgotPasswordEmail(@RequestBody @Valid ForgotPasswordRequest request) {
        emailVerificationService.sendEmail(request.getEmail(), EnumEmailVerificationType.FORGOT_PASSWORD);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email successfully sent"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generate-email-register")
    @Schema(name = "GenerateEmailRegisterRequest", description = "Generate email register request body")
    @Operation(summary = "Endpoint to handle generate email register")
    public ResponseEntity<APIResponse> regenerateEmailRegister(@RequestBody @Valid GenerateEmailRegisterRequest request) {
        emailVerificationService.sendEmail(request.getEmail(), EnumEmailVerificationType.REGISTER);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email successfully regenerated"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

