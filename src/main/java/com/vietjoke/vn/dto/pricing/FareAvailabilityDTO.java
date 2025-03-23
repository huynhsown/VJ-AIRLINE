package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.flight.FlightDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class FareAvailabilityDTO extends BaseDTO {
    private Integer availableSeats;
    private BigDecimal basePrice;
    private FlightDTO flight;
    private FareClassDTO fareClass;
}