package com.binar.byteacademy.dto.response.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiErrorResponse extends APIResponse {
    @Schema(description = "List of errors")
    private List<ErrorDTO> errors;

    public ApiErrorResponse(HttpStatus status, String message, List<ErrorDTO> errors) {
        this.code = status.value();
        this.status = status.name();
        this.message = message;
        this.errors = errors;
    }
}
