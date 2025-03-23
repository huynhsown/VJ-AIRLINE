package com.vietjoke.vn.dto.fleet;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AircraftModelDTO extends BaseDTO {
    private String modelCode;
    private String modelName;
    private int standardCapacity;
    private int premiumCapacity;
    private int businessCapacity;
    private int capacity;
    private List<AircraftDTO> aircrafts;
}