package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.core.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "promo_codes")
public class PromoCodeEntity extends BaseEntity {

    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "is_percentage", nullable = false)
    private Boolean isPercentage = false;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "max_usage")
    private Integer maxUsage;

    @Column(name = "current_usage", nullable = false)
    private Integer currentUsage = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "min_booking_amount", precision = 10, scale = 2)
    private BigDecimal minBookingAmount;

    @Column(name = "min_passenger")
    private Integer minPassenger = 0;

    @Column(name = "is_first_time")
    private Boolean isFirstTime = false;

    @OneToMany(mappedBy = "promoCodeEntity")
    private List<BookingEntity> bookingEntities;
}
