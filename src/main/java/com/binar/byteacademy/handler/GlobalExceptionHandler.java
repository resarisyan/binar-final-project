package com.binar.byteacademy.handler;

import com.binar.byteacademy.dto.response.base.APIResponse;
import com.binar.byteacademy.dto.response.base.ApiErrorResponse;
import com.binar.byteacademy.dto.response.base.ErrorDTO;
import com.binar.byteacademy.exception.AccessDeniedException;
import com.binar.byteacademy.exception.DataConflictException;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                "URL not found: " + ex.getRequestURL()
        );
    }

    @ExceptionHandler(ServiceBusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse handleServiceException(ServiceBusinessException exception) {
        return new APIResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public APIResponse handledDataConflictException(DataConflictException exception) {
        return new APIResponse(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse handledDataNotFoundException(DataNotFoundException exception) {
        return new APIResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse handledDataNotFoundException(AccessDeniedException exception) {
        return new APIResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return new APIResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Schema(name = "ApiErrorResponse", description = "Api error response body")
    public ApiErrorResponse handleMethodArgumentException(MethodArgumentNotValidException exception) {
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDTO errorDTO = new ErrorDTO(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                errors
        );
    }
}