package com.vietjoke.vn.dto.location;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AirportDTO extends BaseDTO {
    private String airportCode;
    private String airportName;
    private String airportEngName;
    private ProvinceDTO province;
}