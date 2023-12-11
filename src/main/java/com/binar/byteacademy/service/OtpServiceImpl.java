package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.common.util.OtpUtil;
import com.binar.byteacademy.dto.request.VerifyRegisterPhoneRequest;
import com.binar.byteacademy.entity.Otp;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumOtpType;
import com.binar.byteacademy.enumeration.EnumStatus;
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
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void verifyRegisterPhoneNumber(VerifyRegisterPhoneRequest request) {
        try {
            userRepository.findFirstByPhoneNumber(request.getPhoneNumber()).filter(user -> !user.isVerifiedPhoneNumber()).ifPresentOrElse(user -> otpRepository.findFirstByOtpCodeAndUserAndOtpType(request.getOtp(), user, EnumOtpType.REGISTER).ifPresentOrElse(otp -> {
                if (otp.getExpTime().isBefore(LocalDateTime.now())) {
                    throw new ForbiddenException("OTP code expired");
                }

                user.setVerifiedPhoneNumber(true);
                userRepository.save(user);
                otp.setUser(null);
                otpRepository.delete(otp);
            }, () -> {
                throw new DataNotFoundException("Wrong OTP Code");
            }), () -> {
                throw new DataNotFoundException("User not found or already verified");
            });
        } catch (DataNotFoundException | ConflictException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to verify account");
        }
    }

    @Override
    @Transactional
    public void verifyChangePhoneNumber(String otpCode) {
        try {
            User user = jwtUtil.getUser();
            otpRepository.findFirstByOtpCodeAndUserAndOtpType(otpCode, user, EnumOtpType.CHANGE_PHONE_NUMBER).ifPresentOrElse(otp -> {
                if (otp.getExpTime().isBefore(LocalDateTime.now())) {
                    throw new ForbiddenException("OTP code expired");
                }

                user.setPhoneNumber(otp.getPhoneNumber());
                user.setOtp(null);
                userRepository.save(user);
                otpRepository.delete(otp);
            }, () -> {
                throw new DataNotFoundException("Wrong OTP Code");
            });
        } catch (DataNotFoundException | ConflictException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to verify account");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void sendOtp(String phoneNumber, EnumOtpType otpType) {
        try {
            String finalPhoneNumber;
            if (EnumOtpType.CHANGE_PHONE_NUMBER.equals(otpType)) {
                finalPhoneNumber = jwtUtil.getUser().getPhoneNumber();
            } else {
                finalPhoneNumber = phoneNumber;
            }

            userRepository.findFirstByPhoneNumber(finalPhoneNumber).filter(user -> {
                if (EnumOtpType.REGISTER.equals(otpType)) {
                    return !user.isVerifiedPhoneNumber();
                } else {
                    return user.isVerifiedPhoneNumber() && user.getStatus().equals(EnumStatus.ACTIVE);
                }
            }).ifPresentOrElse(user -> {
                String message;
                String otpCode = generateOtpCode(user, otpType);

                if (EnumOtpType.REGISTER.equals(otpType)) {
                    message = "Your OTP Code for registration is " + otpCode;
                    otpUtil.sendOtpMessage(finalPhoneNumber, message);
                } else if (EnumOtpType.CHANGE_PHONE_NUMBER.equals(otpType)) {
                    message = "Your OTP Code for changing phone number is " + otpCode;
                    otpUtil.sendOtpMessage(phoneNumber, message);
                } else {
                    message = "Your OTP Code for changing password is " + otpCode;
                    otpUtil.sendOtpMessage(finalPhoneNumber, message);
                }

            }, () -> {
                if (EnumOtpType.REGISTER.equals(otpType)) {
                    throw new ForbiddenException("Phone number already registered");
                } else {
                    throw new DataNotFoundException("Phone number not registered");
                }
            });
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    private String generateOtpCode(User user, EnumOtpType type) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            otpRepository.findFirstByUserAndOtpType(user, type).ifPresent(
                    otp -> {
                        log.info("OTP: {}", otp.getExpTime());
                        log.info("Current: {}", currentDateTime);
                        if (otp.getExpTime().isAfter(currentDateTime)) {
                            throw new ForbiddenException("Please wait for 5 minutes to generate new token");
                        } else {
                            otp.setUser(null);
                            otpRepository.delete(otp);
                        }
                    }
            );
            String otpCode = otpUtil.generateOtp();
            Otp newOtp = Otp.builder()
                    .otpCode(otpCode)
                    .user(user)
                    .otpType(type)
                    .expTime(currentDateTime.plusMinutes(5))
                    .build();
            otpRepository.save(newOtp);
            return otpCode;
        } catch (ForbiddenException e){
            throw e;
        }
        catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}
