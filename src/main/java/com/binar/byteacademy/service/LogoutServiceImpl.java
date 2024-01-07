package com.binar.byteacademy.service;

import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String accessToken;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new DataNotFoundException("Access token is missing");
            }
            accessToken = authHeader.substring(7);
            tokenRepository.findByToken(accessToken).ifPresentOrElse(
                    token -> {
                        token.setExpired(true);
                        token.setRevoked(true);
                        tokenRepository.save(token);
                        SecurityContextHolder.clearContext();
                    },
                    () -> {
                        throw new DataNotFoundException("Access token is invalid");
                    }
            );
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to logout");
        }
    }
}
