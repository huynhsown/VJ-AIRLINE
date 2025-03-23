package com.vietjoke.vn.dto.fleet;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.flight.FlightDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AircraftDTO extends BaseDTO {
    private String registrationNumber;
    private LocalDateTime manufactureDate;
    private Long modelId;
    private Long airlineId;
    private List<FlightDTO> flights;
}