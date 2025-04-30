package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.pricing.SeatResponseDTO;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;

import java.util.List;
import java.util.Map;

public interface SeatReservationService {

    List<SeatReservationEntity> createSeatReservations(FlightEntity flightEntity);
    Integer countAvailableSeat(FlightEntity flightEntity, FareClassEntity fareClassEntity);
    List<SeatResponseDTO> getSeats(String flightNumber, String fareCode);
    ResponseDTO<Map<String, String>> checkSeatSelection(String sessionToken, String flightNumber);

}
