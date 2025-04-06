package com.vietjoke.vn.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vietjoke.vn.dto.user.PassengerDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PassengersInfoParamDTO {
    @NotBlank(message = "Booking Session is required")
    @JsonProperty("sessionToken")
    private String bookingSession;

    @NotEmpty(message = "At least one passenger is required")
    private List<PassengerDTO> passengersAdult = new ArrayList<>();

    private List<PassengerDTO> passengersChild = new ArrayList<>();

    private List<PassengerDTO> passengersInfant = new ArrayList<>();
}
