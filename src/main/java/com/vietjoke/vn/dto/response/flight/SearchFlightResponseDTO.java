package com.vietjoke.vn.dto.response.flight;

import com.vietjoke.vn.util.enums.booking.BookingSessionStep;
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
    private BookingSessionStep currentStep;
    private BookingSessionStep nextStep;
    private LocalDateTime expireAt;
}
