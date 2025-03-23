package com.vietjoke.vn.dto.fleet;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.flight.FlightDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteDTO extends BaseDTO {
    private Integer distance;
    private Integer estimatedDuration;
    private boolean isActive;
    private Long airlineId;
    private Long originAirportId;
    private Long destinationAirportId;
    private List<FlightDTO> flights;
}