package com.vietjoke.vn.dto.response.pricing;

import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class FlightSeatResponseDTO {
    private String sessionToken;
    private Map<String, List<SeatResponseDTO>> flightSeats;
}
