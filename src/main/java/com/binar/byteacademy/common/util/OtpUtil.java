package com.binar.byteacademy.common.util;

import java.util.Random;

import com.binar.byteacademy.exception.ServiceBusinessException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OtpUtil {
    @Value("${twilio.accountId}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String phoneNumber;

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);

        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }

    @Async
    public void sendOtpMessage(String receiver, String otpCode) {
        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new PhoneNumber(receiver),
                    new PhoneNumber(phoneNumber),
                    "Your OTP Code is " + otpCode
            ).create();
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}
