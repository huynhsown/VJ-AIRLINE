package com.vietjoke.vn.repository.pricing;

import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatReservationRepository extends JpaRepository<SeatReservationEntity, Long> {
    Integer countBySeatStatusAndFlightEntityAndFareClassEntity(SeatStatus seatStatus, FlightEntity flightEntity,
                                                               FareClassEntity fareClassEntity);

    @Query("SELECT sr FROM SeatReservationEntity sr WHERE sr.flightEntity.flightNumber = :flightNumber AND sr.fareClassEntity.code = :code")
    List<SeatReservationEntity> findByFlightNumberAndFareCode(
            @Param("flightNumber") String flightNumber,
            @Param("fareCode") String code
    );

    Optional<SeatReservationEntity> findByFlightEntity_FlightNumberAndSeatNumber(String flightNumber, String seatNumber);
}
