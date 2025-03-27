package com.vietjoke.vn.entity.flight;

import com.vietjoke.vn.entity.fleet.AircraftEntity;
import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.entity.fleet.RouteEntity;
import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.booking.BookingDetailEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "flights")
public class FlightEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
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

    @PrePersist
    public void generateFlightNumber() {
        if (this.flightNumber == null || this.flightNumber.isEmpty()) {
            this.flightNumber = "VN" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
    }
}
