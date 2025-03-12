package com.vietjoke.vn.domain.fleet.entity;

import com.vietjoke.vn.domain.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "aircraft_model")
public class AircraftModelEntity extends BaseEntity {
    private String modelCode;
    private String modelName;
    private int standardCapacity;
    private int premiumCapacity;
    private int businessCapacity;
    private int capacity;

    @OneToMany(mappedBy = "aircraftModelEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<AircraftEntity> aircraftEntities;

    @PrePersist
    @PreUpdate
    private void calTotalCapacity(){
        this.capacity = this.standardCapacity + this.premiumCapacity + this.businessCapacity;
    }

}
