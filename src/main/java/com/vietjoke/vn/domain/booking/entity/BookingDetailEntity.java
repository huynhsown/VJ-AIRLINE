package com.vietjoke.vn.domain.booking.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
import com.vietjoke.vn.domain.flight.entity.FlightEntity;
import com.vietjoke.vn.domain.pricing.entity.FareClassEntity;
import com.vietjoke.vn.domain.pricing.entity.SeatReservationEntity;
import com.vietjoke.vn.domain.user.entity.PassengerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "booking_details")
public class BookingDetailEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingEntity bookingEntity;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private PassengerEntity passengerEntity;

    @ManyToOne
    @JoinColumn(name = "fare_class_id", nullable = false)
    private FareClassEntity fareClassEntity;

    @Column(name = "seat_number", length = 10)
    private String seatNumber;

    @Column(name = "fare_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal fareAmount;

    @Column(name = "tax_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal taxAmount;

    @Column(name = "fee_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal feeAmount;

    @Column(name = "discount_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "checked_in", nullable = false)
    private boolean checkedIn = false;

    @Column(name = "checkin_time")
    private LocalDateTime checkinTime;

    @OneToMany(mappedBy = "bookingDetailEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BookingAddonEntity> bookingAddonEntities;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "seat_reservation_id", nullable = true, unique = true)
    private SeatReservationEntity seatReservationEntity;
}
