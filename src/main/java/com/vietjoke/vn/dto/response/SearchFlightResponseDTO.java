package com.vietjoke.vn.dto.response;

import com.vietjoke.vn.dto.fleet.RouteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFlightResponseDTO {
    private List<Map<String, List<FlightResponseDTO>>> travelOptions;
}
