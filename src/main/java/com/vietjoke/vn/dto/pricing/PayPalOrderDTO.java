package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.util.enums.booking.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayPalOrderDTO {
    private String orderId;
    private String status;
    private PaymentMethod paymentMethod = PaymentMethod.PAYPAL;
    private PayPalCaptureDTO capture;
}
