package com.vietjoke.vn.dto.request.flight;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SelectFlightDTO {
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Fare code is required")
    private String fareCode;
}
