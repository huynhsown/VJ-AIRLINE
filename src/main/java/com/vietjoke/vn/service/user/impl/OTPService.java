package com.vietjoke.vn.service.user.impl;

import com.vietjoke.vn.exception.user.OtpCooldownException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${auth.otp-length}")
    private int otpLength;

    @Value("${auth.otp-expiration}")
    private int otpExpiration;

    @Value("${auth.otp-resend-delay}")
    private int otpResendDelay;

    public String generateAndSaveOtp(String email) {
        String lastSentTimeKey = "otp:last-sent:" + email;
        String lastSentTime = redisTemplate.opsForValue().get(lastSentTimeKey);

        if (lastSentTime != null) {
            LocalDateTime lastSent = LocalDateTime.parse(lastSentTime);
            LocalDateTime now = LocalDateTime.now();
            long secondsSinceLastSent = ChronoUnit.SECONDS.between(lastSent, now);

            if (secondsSinceLastSent < otpResendDelay) {
                long remainingSeconds = otpResendDelay - secondsSinceLastSent;
                throw new OtpCooldownException("Vui lòng đợi " + remainingSeconds + " giây trước khi yêu cầu OTP mới");
            }
        }

        String otp = generateOtp();
        redisTemplate.opsForValue().set("otp:" + email, otp, otpExpiration, TimeUnit.SECONDS);

        redisTemplate.opsForValue().set(
                lastSentTimeKey,
                LocalDateTime.now().toString(),
                otpExpiration,
                TimeUnit.SECONDS
        );

        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String otpKey = "otp:" + email;
        String savedOtp = redisTemplate.opsForValue().get(otpKey);

        if (savedOtp != null && savedOtp.equals(otp)) {
            redisTemplate.delete(otpKey);
            return true;
        }
        return false;
    }

    public long getRemainingTimeForResend(String email) {
        String lastSentTimeKey = "otp:last-sent:" + email;
        String lastSentTime = redisTemplate.opsForValue().get(lastSentTimeKey);

        if (lastSentTime == null) {
            return 0;
        }

        LocalDateTime lastSent = LocalDateTime.parse(lastSentTime);
        LocalDateTime now = LocalDateTime.now();
        long secondsSinceLastSent = ChronoUnit.SECONDS.between(lastSent, now);

        return Math.max(0, otpResendDelay - secondsSinceLastSent);
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}