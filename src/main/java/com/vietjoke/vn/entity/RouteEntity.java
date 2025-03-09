package com.vietjoke.vn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "routes")
public class RouteEntity extends BaseEntity{
    private Integer distance;

    private Integer estimatedDuration;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    private AirlineEntity airlineEntity;

    @ManyToOne
    @JoinColumn(name = "origin_airport", nullable = false)
    private AirportEntity originAirportEntity;

    @ManyToOne
    @JoinColumn(name = "destination_airport", nullable = false)
    private AirportEntity destinationAirportEntity;

    @OneToMany(mappedBy = "routeEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<FlightEntity> flightEntities;
}
