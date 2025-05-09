package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "flight_fare_availability")
public class FareAvailabilityEntity extends BaseEntity {
    private Integer availableSeats;
    private BigDecimal basePrice;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "fare_class_id", nullable = false)
    private FareClassEntity fareClassEntity;
}
