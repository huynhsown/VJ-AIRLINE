package com.vietjoke.vn.service.user.impl;

import com.vietjoke.vn.service.helper.EmailTemplateHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }

    public void sendRegistrationEmail(String to, String fullName, String otp) {
        String htmlContent = EmailTemplateHelper.getRegistrationTemplate(fullName, otp);
        sendHtmlEmail(to, "Mã xác thực đăng ký VietJoke Airline", htmlContent);
    }

    public void sendForgotPasswordOTP(String to, String fullName, String otp) {
        String htmlContent = EmailTemplateHelper.getForgotPasswordTemplate(fullName, otp);
        sendHtmlEmail(to, "VietJoke Airline", htmlContent);
    }
}