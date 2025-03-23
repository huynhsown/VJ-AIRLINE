package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.booking.BookingDetailEntity;
import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
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
