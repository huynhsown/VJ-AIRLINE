package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.dto.response.ResponseDTO;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.Map;

public interface PaymentService {

    ResponseDTO<Map<String, String>> createPaymentOrder(String sessionToken) throws IOException;

    ResponseDTO<Map<String, String>> captureOrder(String sessionToken, String orderId, String username) throws IOException;
}
