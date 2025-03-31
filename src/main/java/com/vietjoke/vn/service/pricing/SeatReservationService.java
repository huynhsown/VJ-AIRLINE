package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;

import java.util.List;

public interface SeatReservationService {

    List<SeatReservationEntity> createSeatReservations(FlightEntity flightEntity);
    Integer countAvailableSeat(FlightEntity flightEntity, FareClassEntity fareClassEntity);

}
