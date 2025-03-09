package com.vietjoke.vn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "flights")
public class FlightEntity extends BaseEntity{
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
}
