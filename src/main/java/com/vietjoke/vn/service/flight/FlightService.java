package com.vietjoke.vn.service.flight;

import com.vietjoke.vn.config.seeding.jsonObject.Flight;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.SearchFlightResponseDTO;

public interface FlightService {
    void createOrUpdateFlight(Flight flight);
    ResponseDTO<SearchFlightResponseDTO> searchFlight(SearchParamDTO searchParam);
}
