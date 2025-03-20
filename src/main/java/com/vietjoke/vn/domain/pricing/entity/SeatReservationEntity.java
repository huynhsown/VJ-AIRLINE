package com.vietjoke.vn.domain.pricing.entity;

import com.vietjoke.vn.domain.booking.entity.BookingDetailEntity;
import com.vietjoke.vn.domain.core.entity.BaseEntity;
import com.vietjoke.vn.domain.flight.entity.FlightEntity;
import com.vietjoke.vn.domain.pricing.enums.AddonType;
import com.vietjoke.vn.domain.pricing.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "seat_reservations")
public class SeatReservationEntity extends BaseEntity {
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status")
    private SeatStatus seatStatus;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "fare_class_id")
    private FareClassEntity fareClassEntity;

    @OneToOne(mappedBy = "seatReservationEntity", cascade = CascadeType.PERSIST)
    private BookingDetailEntity bookingDetailEntity;

}
