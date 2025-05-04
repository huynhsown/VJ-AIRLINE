package com.vietjoke.vn.service.flight;

import com.vietjoke.vn.config.seeding.jsonObject.Flight;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.SearchFlightResponseDTO;
import com.vietjoke.vn.entity.flight.FlightEntity;

public interface FlightService {
    FlightEntity getFlightByFlightNumber(String flightNumber);
    void createOrUpdateFlight(Flight flight);
    ResponseDTO<SearchFlightResponseDTO> searchFlight(SearchParamDTO searchParam);
    ResponseDTO<?> selectFlight(SelectFlightRequestDTO selectParam);
    ResponseDTO<?> getSeatOfFlight(String sessionToken);
}
