package com.vietjoke.vn.service.booking.impl;

import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.booking.BookingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secretKey;

    @Value("${paypal.api-url}")
    private String apiUrl;

    private final BookingSessionService bookingSessionService;

    public String getAccessToken() throws IOException {
        String auth = clientId + ":" + secretKey;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        URL url = new URL(apiUrl + "/v1/oauth2/token");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String data = "grant_type=client_credentials";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new IOException("Failed to get access token, status: " + status);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString().split("\"access_token\":\"")[1].split("\"")[0];
        }
    }

    @Override
    public String createPayPalOrder(BigDecimal amount) throws IOException {

        String accessToken = getAccessToken();
        String currency = "USD";
        URL url = new URL(apiUrl + "/v2/checkout/orders");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Prefer", "return=representation");

        String payload = "{"
                + "\"intent\": \"CAPTURE\","
                + "\"purchase_units\": [{"
                + "\"amount\": {"
                + "\"currency_code\": \"" + currency + "\","
                + "\"value\": \"" + amount + "\""
                + "}}]}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        if (status != 201) {
            throw new IOException("Failed to create order, status: " + status);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString().split("\"id\":\"")[1].split("\"")[0]; //Get order Id
        }
    }
}
