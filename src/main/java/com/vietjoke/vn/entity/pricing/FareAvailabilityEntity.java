package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "flight_fare_availability")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FareAvailabilityEntity extends BaseEntity {
    private Integer availableSeats;
    private BigDecimal basePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "fare_class_id", nullable = false)
    private FareClassEntity fareClassEntity;
}
