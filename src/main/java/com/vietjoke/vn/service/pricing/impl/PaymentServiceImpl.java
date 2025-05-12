package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.dto.pricing.PayPalOrderDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.exception.pricing.InvalidPriceException;
import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
import com.vietjoke.vn.service.helper.PriceCalculator;
import com.vietjoke.vn.service.pricing.PaymentService;
import com.vietjoke.vn.service.user.UserService;
import com.vietjoke.vn.util.enums.booking.PaymentMethod;
import com.vietjoke.vn.util.enums.booking.PaymentStatus;
import com.vietjoke.vn.util.paypal.PayPalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingSessionService bookingSessionService;
    private final PriceCalculator priceCalculator;
    private final PayPalUtil payPalUtil;
    private final BookingService bookingService;
    private final UserService userService;

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
        UserEntity userEntity = userService.getUserByUsername(username);

        Map<String, String> result = new HashMap<>();

        PayPalOrderDTO payPalOrderDTO = payPalUtil.captureOrder(orderId);
        if(Objects.equals(payPalOrderDTO.getStatus(), PaymentStatus.COMPLETED.toString())) {
            BookingEntity bookingEntity = bookingService.createBooking(
                    sessionToken, userEntity, payPalOrderDTO
            );
            bookingSessionService.deleteSession(sessionToken);
            result.put("orderId", orderId);
            result.put("status", PaymentStatus.COMPLETED.toString());
            return ResponseDTO.success(result);
        }

        result.put("orderId", orderId);
        result.put("status", PaymentStatus.FAILED.toString());

        return ResponseDTO.success(result);
    }

}
