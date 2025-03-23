package com.vietjoke.vn.dto.flight;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlightStatusDTO extends BaseDTO {
    private String statusCode;
    private String statusName;
    private List<FlightDTO> flights;
}