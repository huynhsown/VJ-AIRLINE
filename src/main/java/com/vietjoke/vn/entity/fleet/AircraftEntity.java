package com.vietjoke.vn.entity.fleet;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
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
