package com.vietjoke.vn.dto.request.pricing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SeatReservationRequestDTO {
    @NotBlank(message = "Flight number cannot be blank")
    private String flightNumber;

    @NotBlank(message = "Seat number cannot be blank")
    private String seatNumber;

    @NotBlank(message = "Passenger UUID cannot be blank")
    private String passengerUUID;

    @NotBlank(message = "Session token cannot be blank")
    private String sessionToken;
}