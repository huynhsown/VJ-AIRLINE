package com.vietjoke.vn.dto.response.flight;

import com.vietjoke.vn.dto.fleet.RouteDTO;
import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.dto.response.FareClassDTO;
import com.vietjoke.vn.dto.response.pricing.SeatResponseDTO;
import lombok.Data;
import org.springframework.data.domain.Page;

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
    private List<SeatResponseDTO> flightSeats;
    private Page<AddonDTO> addonDTOs;
}
