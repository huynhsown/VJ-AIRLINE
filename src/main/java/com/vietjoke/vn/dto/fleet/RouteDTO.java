package com.vietjoke.vn.dto.fleet;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.flight.FlightDTO;
import com.vietjoke.vn.dto.location.AirportDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteDTO extends BaseDTO {
    private String routeCode;
    private Integer distance;
    private Integer estimatedDuration;
    private AirportDTO originAirport;
    private AirportDTO destinationAirport;
}