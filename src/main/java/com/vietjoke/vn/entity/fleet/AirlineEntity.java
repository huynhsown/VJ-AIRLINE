package com.vietjoke.vn.entity.fleet;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
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
