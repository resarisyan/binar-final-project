package com.binar.byteacademy.controller;

import com.binar.byteacademy.dto.request.*;
import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.enumeration.EnumEmailVerificationType;
import com.binar.byteacademy.enumeration.EnumOtpType;
import com.binar.byteacademy.service.AuthenticationService;
import com.binar.byteacademy.service.EmailVerificationService;
import com.binar.byteacademy.service.OtpService;
import com.binar.byteacademy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.binar.byteacademy.common.util.Constants.SettingPats.SETTING_PATS;

@RestController
@RequestMapping(value = SETTING_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Setting", description = "Setting API")
public class SettingController {
    private final AuthenticationService authenticationService;
    private final OtpService otpService;
    private final EmailVerificationService emailVerificationService;
    private final UserService userService;

    @PostMapping("/change-password")
    @Schema(name = "ChangePasswordRequest", description = "Change password request body")
    @Operation(summary = "Endpoint to handle change password")
    public ResponseEntity<APIResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal connectedUser) {
        authenticationService.changePassword(request, connectedUser);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Password successfully changed"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-change-phone")
    @Schema(name = "VerifyChangePhoneRequest", description = "Verify change phone request body")
    @Operation(summary = "Endpoint to handle verify change phone")
    public ResponseEntity<APIResponse> verifyAccount(@RequestParam String otp) {
        otpService.verifyChangePhoneNumber(otp);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Account successfully verified"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-change-email")
    @Schema(name = "VerifyChangeEmailRequest", description = "Verify change email request body")
    @Operation(summary = "Endpoint to handle verify change email")
    public ResponseEntity<APIResponse> verifyEmailChange(@RequestBody @Valid EmailTokenRequest request) {
        emailVerificationService.verifyChangeEmailToken(request.getToken());
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email successfully verified"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generate-email-change")
    @Schema(name = "GenerateEmailChangeRequest", description = "Generate email change request body")
    @Operation(summary = "Endpoint to handle generate email change")
    public ResponseEntity<APIResponse> generateEmailChange(@RequestBody @Valid GenerateEmailChangeRequest request) {
        emailVerificationService.sendEmail(request.getEmail(), EnumEmailVerificationType.CHANGE_EMAIL);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email successfully regenerated"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generate-otp-change-phone")
    @Schema(name = "GenerateOtpChangePhoneRequest", description = "Generate otp change phone request body")
    @Operation(summary = "Endpoint to handle generate otp change phone")
    public ResponseEntity<APIResponse> regenerateOtpChangePhone(@RequestBody @Valid GenerateOtpChangePhoneRequest request) {
        otpService.sendOtp(request.getPhoneNumber(), EnumOtpType.CHANGE_PHONE_NUMBER);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "OTP Change Phone successfully regenerated"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-customer-detail")
    @Schema(name = "UpdateCustomerDetailRequest", description = "Update customer detail request body")
    @Operation(summary = "Endpoint to handle update customer detail")
    public ResponseEntity<APIResponse> updateCustomerDetail(@RequestBody @Valid UpdateCustomerDetailRequest request, Principal principal) {
        userService.updateCustomerDetail(request, principal);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Customer detail successfully updated"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
