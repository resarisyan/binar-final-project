package com.binar.byteacademy.dto.response.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResultResponse<T> {
    @Schema(description = "Results data (if any)")
    private T results;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Schema(description = "Timestamp of the response")
    protected Date timestamp;

    @Schema(description = "Status of the response")
    protected String status;

    @Schema(description = "HTTP status code of the response")
    protected int code;

    @Schema(description = "Message associated with the response")
    protected String message;

    public APIResultResponse(HttpStatus status, String message) {
        this.status = status.name();
        this.code = status.value();
        this.message = message;
    }

    public APIResultResponse(HttpStatus status, String message, T results) {
        this.status = status.name();
        this.code = status.value();
        this.results = results;
        this.message = message;
    }
}
