package com.vietjoke.vn.dto.response.pricing;

import com.vietjoke.vn.dto.response.flight.FlightResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FlightServiceResponseDTO {
    private String sessionToken;
    private List<FlightResponseDTO> flight;
}
