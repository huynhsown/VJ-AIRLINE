package com.vietjoke.vn.util.paypal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.dto.pricing.PayPalCaptureDTO;
import com.vietjoke.vn.dto.pricing.PayPalOrderDTO;
import com.vietjoke.vn.util.enums.booking.PaymentMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class PayPalUtil {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secretKey;

    @Value("${paypal.api-url}")
    private String apiUrl;

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

    public PayPalOrderDTO getOrderDetails(String orderId) throws IOException {
        String accessToken = getAccessToken();
        URL url = new URL(apiUrl + "/v2/checkout/orders/" + orderId);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new IOException("Failed to get order details, status: " + status);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());

            PayPalOrderDTO orderDTO = PayPalOrderDTO.builder()
                    .orderId(rootNode.get("id").asText())
                    .status(rootNode.get("status").asText())
                    .build();

            try {
                JsonNode purchaseUnits = rootNode.get("purchase_units");
                if (purchaseUnits != null && purchaseUnits.isArray() && purchaseUnits.size() > 0) {
                    JsonNode payments = purchaseUnits.get(0).get("payments");
                    if (payments != null) {
                        JsonNode captures = payments.get("captures");
                        if (captures != null && captures.isArray() && captures.size() > 0) {
                            JsonNode capture = captures.get(0);
                            JsonNode amount = capture.get("amount");

                            PayPalCaptureDTO captureDTO = PayPalCaptureDTO.builder()
                                    .captureId(capture.get("id").asText())
                                    .captureStatus(capture.get("status").asText())
                                    .amount(amount != null ? new BigDecimal(amount.get("value").asText()) : null)
                                    .currency(amount != null ? amount.get("currency_code").asText() : null)
                                    .build();

                            orderDTO.setCapture(captureDTO);
                        } else {
                            orderDTO.setCapture(PayPalCaptureDTO.builder()
                                    .captureId(null)
                                    .captureStatus("NO_CAPTURE")
                                    .build());
                        }
                    }
                }
            } catch (Exception e) {
                orderDTO.setCapture(PayPalCaptureDTO.builder()
                        .captureId(null)
                        .captureStatus("ERROR")
                        .build());
            }

            return orderDTO;
        }
    }

    public PayPalOrderDTO captureOrder(String orderId) throws IOException {
        String accessToken = getAccessToken();
        URL url = new URL(apiUrl + "/v2/checkout/orders/" + orderId + "/capture");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Prefer", "return=representation");

        int status = connection.getResponseCode();
        if (status != 201) {
            throw new IOException("Failed to capture order, status: " + status);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());

            PayPalOrderDTO orderDTO = PayPalOrderDTO.builder()
                    .orderId(rootNode.get("id").asText())
                    .status(rootNode.get("status").asText())
                    .paymentMethod(PaymentMethod.PAYPAL)
                    .build();

            try {
                JsonNode purchaseUnits = rootNode.get("purchase_units");
                if (purchaseUnits != null && purchaseUnits.isArray() && purchaseUnits.size() > 0) {
                    JsonNode payments = purchaseUnits.get(0).get("payments");
                    if (payments != null) {
                        JsonNode captures = payments.get("captures");
                        if (captures != null && captures.isArray() && captures.size() > 0) {
                            JsonNode capture = captures.get(0);
                            JsonNode amount = capture.get("amount");

                            PayPalCaptureDTO captureDTO = PayPalCaptureDTO.builder()
                                    .captureId(capture.get("id").asText())
                                    .captureStatus(capture.get("status").asText())
                                    .amount(amount != null ? new BigDecimal(amount.get("value").asText()) : null)
                                    .currency(amount != null ? amount.get("currency_code").asText() : null)
                                    .build();

                            orderDTO.setCapture(captureDTO);
                        }
                    }
                }
            } catch (Exception e) {
                orderDTO.setCapture(PayPalCaptureDTO.builder()
                        .captureId(null)
                        .captureStatus("ERROR")
                        .build());
            }

            return orderDTO;
        }
    }
}
