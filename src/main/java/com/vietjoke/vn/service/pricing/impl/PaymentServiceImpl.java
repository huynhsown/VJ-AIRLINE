package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.pricing.InvalidPriceException;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
import com.vietjoke.vn.service.helper.PriceCalculator;
import com.vietjoke.vn.service.pricing.PaymentService;
import com.vietjoke.vn.util.enums.booking.PaymentMethod;
import com.vietjoke.vn.util.enums.booking.PaymentStatus;
import com.vietjoke.vn.util.paypal.PayPalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingSessionService bookingSessionService;
    private final PriceCalculator priceCalculator;
    private final PayPalUtil payPalUtil;

    @Override
    public ResponseDTO<Map<String, String>> createPaymentOrder(String sesionToken) throws IOException {
        BookingSession session = bookingSessionService.getSession(sesionToken);
        BookingSessionHelper.validatePreviewBookingSteps(session);
        BigDecimal paymentPrice = priceCalculator.calculatePaymentPrice(session);

        if (paymentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Invalid payment price");
        }

        String orderId = payPalUtil.createPayPalOrder(paymentPrice);

        return ResponseDTO.success(Map.of(
                "sessionToken", session.getSessionId(),
                "orderId", orderId
        ));
    }

    @Override
    public ResponseDTO<Map<String, String>> captureOrder(String sessionToken, String orderId, String username) throws IOException {
        BookingSession session = bookingSessionService.getSession(sessionToken);
        BookingSessionHelper.validatePreviewBookingSteps(session);

        Map<String, String> orderDetail = payPalUtil.getOrderDetails(orderId);
        orderDetail.put("payment_method", PaymentMethod.PAYPAL.name());
        if(Objects.equals(orderDetail.get("status"), PaymentStatus.COMPLETED.toString())) {

        }
        return null;
    }


}
