package com.vietjoke.vn.dto.flight;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.fleet.AircraftDTO;
import com.vietjoke.vn.dto.fleet.AirlineDTO;
import com.vietjoke.vn.dto.fleet.RouteDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlightDTO extends BaseDTO {
    private String flightNumber;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private String gate;
    private String terminal;
    private RouteDTO route;
    private FlightStatusDTO flightStatus;
}