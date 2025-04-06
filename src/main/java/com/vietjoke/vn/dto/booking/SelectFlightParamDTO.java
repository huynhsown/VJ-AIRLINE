package com.vietjoke.vn.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SelectFlightParamDTO {
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Fare code is required")
    private String fareCode;
}
