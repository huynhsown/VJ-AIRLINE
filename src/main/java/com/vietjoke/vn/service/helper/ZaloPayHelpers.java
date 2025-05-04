package com.vietjoke.vn.service.helper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.vietjoke.vn.service.helper.HMac.HMacUtil;
import org.springframework.stereotype.Service;

@Service
public class ZaloPayHelpers {
    private static int transIdDefault = 1;

    /**
     * Method to generate transaction ID
     * @return Generated transaction ID
     */
    public static String getAppTransId() {
        if (transIdDefault >= 100000) {
            transIdDefault = 1;
        }

        transIdDefault += 1;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_hhmmss");
        String timeString = LocalDateTime.now().format(formatter);

        return String.format("%s%06d", timeString, transIdDefault);
    }

    /**
     * Method to generate HMAC using SHA-256
     * @param key The secret key
     * @param data The data to be hashed
     * @return The HMAC hash in hex string format
     * @throws NoSuchAlgorithmException If the algorithm is not available
     * @throws InvalidKeyException If the key is invalid
     */
    public static String getMac(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        return Objects.requireNonNull(HMacUtil.HMacHexStringEncode(HMacUtil.HMACSHA256, key, data));
    }
}
