package com.vietjoke.vn.dto.fleet;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.flight.FlightDTO;
import com.vietjoke.vn.dto.pricing.FareClassDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AirlineDTO extends BaseDTO {
    private String airlineCode;
    private String airlineName;
    private List<AircraftDTO> aircrafts;
    private List<FareClassDTO> fareClasses;
    private List<FlightDTO> flights;
}