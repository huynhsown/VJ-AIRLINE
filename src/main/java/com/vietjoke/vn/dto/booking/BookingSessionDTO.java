package com.vietjoke.vn.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.user.PassengerInfoResponseDTO;
import lombok.Data;

@Data
public class BookingSessionDTO {
    @JsonProperty("sessionToken")
    private String sessionId;
    private SearchParamDTO searchCriteria;
    private SelectFlightRequestDTO selectedFlight;
    private PassengerInfoResponseDTO passengerInfo;
}
