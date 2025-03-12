package com.vietjoke.vn.domain.fleet.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
import com.vietjoke.vn.domain.pricing.entity.FareClassEntity;
import com.vietjoke.vn.domain.flight.entity.FlightEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "airlines")
public class AirlineEntity extends BaseEntity {
    private String airlineCode;
    private String airlineName;

    @OneToMany(mappedBy = "airlineEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<AircraftEntity> aircraftEntities;

    @OneToMany(mappedBy = "airlineEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<FareClassEntity> fareClassEntities;

    @OneToMany(mappedBy = "airlineEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<FlightEntity> flightEntities;
}
