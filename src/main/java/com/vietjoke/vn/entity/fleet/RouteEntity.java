package com.vietjoke.vn.entity.fleet;

import com.vietjoke.vn.entity.location.AirportEntity;
import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "routes",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "origin_airport", "destination_airport" }) })
public class RouteEntity extends BaseEntity {

    private String routeCode;

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
