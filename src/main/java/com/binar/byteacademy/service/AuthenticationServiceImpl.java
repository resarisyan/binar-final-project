package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ChangePasswordRequest;
import com.binar.byteacademy.dto.request.LoginRequest;
import com.binar.byteacademy.dto.request.RegisterRequest;
import com.binar.byteacademy.dto.response.LoginResponse;
import com.binar.byteacademy.dto.response.RefreshTokenResponse;
import com.binar.byteacademy.dto.response.RegisterResponse;
import com.binar.byteacademy.entity.CustomerDetail;
import com.binar.byteacademy.entity.Token;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.*;
import com.binar.byteacademy.exception.*;
import com.binar.byteacademy.repository.CustomerDetailRepository;
import com.binar.byteacademy.repository.TokenRepository;
import com.binar.byteacademy.repository.UserRepository;
import com.binar.byteacademy.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final CustomerDetailRepository customerDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            boolean isEmail = request.getCredential().contains("@");
            User user;
            if (isEmail) {
                user = userRepository.findFirstByEmail(request.getCredential()).orElseThrow(
                        () -> new DataNotFoundException("User not found")
                );
            } else {
                user = userRepository.findFirstByUsername(request.getCredential()).orElseThrow(
                        () -> new DataNotFoundException("User not found")
                );
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword()
                    )
            );

            if (!user.isVerifiedPhoneNumber()) {
                throw new UserNotActiveException("User is not verified");
            }
            if (user.getStatus() == EnumStatus.INACTIVE) {
                throw new UserNotActiveException("User is not active");
            }

            String jwtToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return LoginResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (DataNotFoundException | UserNotActiveException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(EnumRole.CUSTOMER)
                    .status(EnumStatus.ACTIVE)
                    .build();
            user = userRepository.save(user);

            CustomerDetail customerDetail = CustomerDetail.builder()
                    .name(request.getName())
                    .user(user)
                    .build();
            customerDetailRepository.save(customerDetail);

            emailVerificationService.sendEmail(user.getEmail(), EnumEmailVerificationType.REGISTER);
            otpService.sendOtp(user.getPhoneNumber(), EnumOtpType.REGISTER);

            return RegisterResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .name(customerDetail.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        } catch (DataConflictException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new ValidationException("Current password is wrong");
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new ValidationException("Password are not the same");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        RefreshTokenResponse refreshTokenResponse;

        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String refreshToken;
            final String username;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new DataNotFoundException("Refresh token is missing");
            }
            refreshToken = authHeader.substring(7);
            username = jwtUtil.extractUsername(refreshToken);
            if (username == null) {
                throw new ServiceBusinessException("Refresh token is invalid");
            }

            User user = this.userRepository.findFirstByUsername(username)
                    .orElseThrow();
            if (jwtUtil.isTokenValid(refreshToken, user)) {
                String accessToken = jwtUtil.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                refreshTokenResponse = RefreshTokenResponse.builder()
                        .accessToken(accessToken)
                        .build();
            } else {
                throw new ServiceBusinessException("Refresh token is invalid");
            }
        } catch (DataNotFoundException | ServiceBusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to refresh token");
        }
        return refreshTokenResponse;
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(EnumTokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
