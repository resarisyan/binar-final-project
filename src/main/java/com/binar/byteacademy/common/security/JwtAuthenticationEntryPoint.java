package com.binar.byteacademy.common.security;

import com.binar.byteacademy.dto.response.base.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        APIResponse apiResponse = new APIResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, apiResponse);
        out.flush();
    }
}

