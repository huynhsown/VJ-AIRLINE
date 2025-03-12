package com.vietjoke.vn.domain.fleet.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
import com.vietjoke.vn.domain.flight.entity.FlightEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "aircraft")
public class AircraftEntity extends BaseEntity {
    private String registrationNumber;
    private LocalDateTime manufactureDate;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private AircraftModelEntity aircraftModelEntity;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    private AirlineEntity airlineEntity;

    @OneToMany(mappedBy = "aircraftEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<FlightEntity> flightEntities;
}
