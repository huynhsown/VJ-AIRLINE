package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FareAvailabilityRepository extends JpaRepository<FareAvailabilityEntity, Long> {

    Optional<FareAvailabilityEntity> findByFlightEntityAndFareClassEntity(FlightEntity flightEntity,
                                                                          FareClassEntity fareClassEntity);

}
