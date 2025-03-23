package com.vietjoke.vn.entity.flight;

import com.vietjoke.vn.entity.core.BaseEntity;
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
