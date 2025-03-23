package com.vietjoke.vn.dto.location;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.fleet.RouteDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AirportDTO extends BaseDTO {
    private String airportCode;
    private String airportName;
    private String airportEngName;
    private String timeZone;
    private String utcOffset;
    private ProvinceDTO province;
    private List<RouteDTO> routeOrigins;
    private List<RouteDTO> routeDestinations;
}