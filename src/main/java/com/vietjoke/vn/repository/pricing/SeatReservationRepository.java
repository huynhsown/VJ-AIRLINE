package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatReservationRepository extends JpaRepository<SeatReservationEntity, Long> {
    Integer countBySeatStatusAndFlightEntityAndFareClassEntity(SeatStatus seatStatus, FlightEntity flightEntity,
                                                               FareClassEntity fareClassEntity);
}
