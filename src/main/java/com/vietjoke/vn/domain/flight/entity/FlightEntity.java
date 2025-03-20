package com.vietjoke.vn.domain.flight.entity;

import com.vietjoke.vn.domain.fleet.entity.AircraftEntity;
import com.vietjoke.vn.domain.fleet.entity.AirlineEntity;
import com.vietjoke.vn.domain.fleet.entity.RouteEntity;
import com.vietjoke.vn.domain.core.entity.BaseEntity;
import com.vietjoke.vn.domain.booking.entity.BookingDetailEntity;
import com.vietjoke.vn.domain.pricing.entity.SeatReservationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "flights")
public class FlightEntity extends BaseEntity {
    private String flightNumber;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private String gate;
    private String terminal;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private AirlineEntity airlineEntity;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private RouteEntity routeEntity;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraftEntity;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private FlightStatusEntity flightStatusEntity;

    @OneToMany(mappedBy = "flightEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetailEntity> bookingDetailEntities;

    @OneToMany(mappedBy = "flightEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatReservationEntity> seatReservationEntities;
}
