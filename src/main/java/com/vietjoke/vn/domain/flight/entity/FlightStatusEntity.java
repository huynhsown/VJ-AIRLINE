package com.vietjoke.vn.domain.flight.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
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
@Table(name = "flight_status")
public class FlightStatusEntity extends BaseEntity {
    private String statusCode;
    private String statusName;

    @OneToMany(mappedBy = "flightStatusEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<FlightEntity> flightEntities;
}
