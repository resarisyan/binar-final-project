package com.binar.byteacademy.common.util;

import com.binar.byteacademy.exception.ServiceBusinessException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmailResetAsync(String email, String resetLink) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true);
            msg.setFrom("noreply@byteacademy.com");
            msg.setTo(email);
            msg.setSubject("Reset Password");
            msg.setText("Hello \n\n" + "Please click on this link to Reset your Password: " + resetLink + ". \n\n"
                    + "Regards \n" + "Byte Academy");
            msg.setSentDate(new Date());
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to send email");
        }
    }

    @Async
    public void sendEmailVerificationAsync(String email, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true);
            msg.setFrom("noreply@byteacademy.com");
            msg.setTo(email);
            msg.setSubject(subject);
            msg.setText(text);
            msg.setSentDate(new Date());
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to send email");
        }
    }
}
