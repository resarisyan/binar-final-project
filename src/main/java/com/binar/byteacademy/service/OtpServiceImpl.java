package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.OtpUtil;
import com.binar.byteacademy.entity.Otp;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.ConflictException;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ForbiddenException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.OtpRepository;
import com.binar.byteacademy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final OtpUtil otpUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void verifyAccount(String otpCode, String username) {
        try {
            User user = userRepository.findFirstByUsername(username).orElseThrow(
                    () -> new DataNotFoundException("User not found")
            );
            if (user.isVerified()) {
                throw new ConflictException("User already verified");
            }

            Otp otp = otpRepository.findFirstByOtpCodeAndUser(otpCode, user).orElseThrow(
                    () -> new DataNotFoundException("Wrong OTP Code")
            );

            LocalDateTime expirationTime = otp.getExpTime();
            LocalDateTime fiveMinutesLater = expirationTime.plusMinutes(5);

            if (fiveMinutesLater.isBefore(LocalDateTime.now())) {
                throw new ForbiddenException("OTP Code expired");
            }

            user.setOtp(null);
            user.setVerified(true);
            userRepository.save(user);
            otpRepository.delete(otp);
        } catch (DataNotFoundException | ConflictException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to verify account");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void generateOtp(String username, String phoneNumber) {
        try {
            userRepository.findFirstByUsername(username)
                    .filter(u -> !u.isVerified())
                    .ifPresentOrElse(
                            user -> {
                                String otpCode = otpUtil.generateOtp();
                                String receiver = phoneNumber != null ? phoneNumber : user.getCustomerDetail().getPhoneNumber();
                                if (phoneNumber != null) {
                                    Otp newOtp = Otp.builder()
                                            .otpCode(otpCode)
                                            .user(user)
                                            .expTime(LocalDateTime.now())
                                            .build();
                                    otpRepository.save(newOtp);
                                } else {
                                    if (user.getOtp() != null) {
                                        LocalDateTime expirationTime = user.getOtp().getExpTime();
                                        if (expirationTime.plusMinutes(5).isAfter(LocalDateTime.now())) {
                                            throw new ForbiddenException("OTP Code still valid");
                                        }
                                        user.getOtp().setOtpCode(otpCode);
                                        user.getOtp().setExpTime(LocalDateTime.now());
                                        otpRepository.save(user.getOtp());
                                    } else {
                                        throw new DataNotFoundException("OTP not found");
                                    }
                                }
                                otpUtil.sendOtpMessage(receiver, otpCode);
                            },
                            () -> {
                                throw new DataNotFoundException("User not found or already verified");
                            }
                    );
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to generate OTP");
        }
    }
}
