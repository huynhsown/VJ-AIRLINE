package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.util.enums.booking.PaymentMethod;
import com.vietjoke.vn.util.enums.booking.PaymentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookingPaymentDTO extends BaseDTO {
    private Long bookingId;
    private BigDecimal paymentAmount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private LocalDateTime paymentDate;
}