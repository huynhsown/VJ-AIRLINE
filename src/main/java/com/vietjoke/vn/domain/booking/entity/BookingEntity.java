package com.vietjoke.vn.domain.booking.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
import com.vietjoke.vn.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class BookingEntity extends BaseEntity {

    @Column(name = "booking_reference", length = 20, nullable = false, unique = true)
    private String bookingReference;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "booking_date", nullable = false, updatable = false)
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 10, nullable = false)
    private String currency = "VND";

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "payment_reference", length = 50)
    private String paymentReference;

    @Column(name = "promo_code", length = 20)
    private String promoCode;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "adult_count", nullable = false)
    private int adultCount = 0;

    @Column(name = "child_count", nullable = false)
    private int childCount = 0;

    @Column(name = "infant_count", nullable = false)
    private int infantCount = 0;

    @Column(name = "trip_type", length = 20)
    private String tripType;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private BookingStatusEntity bookingStatusEntity;

    @OneToMany(mappedBy = "bookingEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BookingDetailEntity> bookingDetailEntities;

    @OneToMany(mappedBy = "bookingEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BookingPaymentEntity> bookingPaymentEntities;
}
