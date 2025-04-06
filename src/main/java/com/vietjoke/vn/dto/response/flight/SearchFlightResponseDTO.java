package com.vietjoke.vn.dto.response.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFlightResponseDTO {
    private List<Map<String, List<FlightResponseDTO>>> travelOptions;
    private String sessionToken;
    private LocalDateTime expireAt;
}
