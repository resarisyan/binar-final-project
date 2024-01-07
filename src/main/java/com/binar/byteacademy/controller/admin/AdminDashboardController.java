package com.binar.byteacademy.controller.admin;

import com.binar.byteacademy.dto.response.DashboardResponse;
import com.binar.byteacademy.dto.response.base.APIResultResponse;
import com.binar.byteacademy.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.binar.byteacademy.common.util.Constants.DashboardPats.ADMIN_DASHBOARD_PATS;

@RestController
@RequestMapping(value = ADMIN_DASHBOARD_PATS, produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard", description = "Admin Dashboard")
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    @Schema(name = "getDashboard", description = "Get Admin Dashboard")
    @Operation(summary = "Get Admin Dashboard", tags = {"Admin Dashboard"})
    public ResponseEntity<APIResultResponse<DashboardResponse>> getDashboard() {
        DashboardResponse dashboardResponse = dashboardService.getAdminDashboard();
        APIResultResponse<DashboardResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Dashboard successfully retrieved",
                dashboardResponse
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
