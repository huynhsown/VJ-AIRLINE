package com.vietjoke.vn.dto.response;

import com.vietjoke.vn.dto.fleet.RouteDTO;
import com.vietjoke.vn.dto.location.AirportDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class FlightResponseDTO {
    private String flightNumber;
    private String flightModelCode;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private List<FareClassDTO> fareClasses = new ArrayList<>();
    private RouteDTO route;
}
