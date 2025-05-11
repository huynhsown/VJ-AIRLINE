package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.service.pricing.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payment/create-order")
    public ResponseEntity<?> createOrder(@RequestParam String sessionToken) throws IOException {
        return ResponseEntity.ok(paymentService.createPaymentOrder(sessionToken));
    }

    @PostMapping("/payment/capture-order")
    public ResponseEntity<?> captureOrder(
            @RequestParam String sessionToken,
            @RequestParam String orderId,
            @AuthenticationPrincipal UserDetails userDetails
            ) throws IOException {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(paymentService.captureOrder(
                sessionToken,
                orderId,
                username));
    }
}
