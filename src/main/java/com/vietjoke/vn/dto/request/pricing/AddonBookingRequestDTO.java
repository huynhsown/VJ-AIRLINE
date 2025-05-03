package com.vietjoke.vn.dto.request.pricing;

import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddonBookingRequestDTO {
    @NotBlank(message = "Session Token is required")
    private String sessionToken;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Passenger UUID is required")
    private String passengerUuid;

    @Valid
    private List<AddonSelectionDTO> addons;
}

