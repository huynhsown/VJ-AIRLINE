package com.vietjoke.vn.entity.booking;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.util.enums.booking.PaymentMethod;
import com.vietjoke.vn.util.enums.booking.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "booking_payments")
public class BookingPaymentEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingEntity bookingEntity;

    @Column(name = "payment_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20, nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20, nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

    @Column(name = "transaction_id", length = 255, unique = true)
    private String transactionId;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();
}
