package com.vietjoke.vn.dto.response.pricing;

import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PassengerAddonsResponseDTO {
    private String passengerUuid;
    private String flightNumber;
    private List<AddonSelectionDTO> addons;
}
