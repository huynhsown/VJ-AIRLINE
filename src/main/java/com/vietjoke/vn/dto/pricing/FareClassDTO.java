package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.fleet.AirlineDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FareClassDTO extends BaseDTO {
    private String code;
    private String name;
    private String description;
    private Boolean isActive;
    private Integer baggageAllowance;
    private Boolean seatSelection;
    private Boolean mealIncluded;
    private Boolean changeAllowed;
    private Boolean refundAllowed;
    private Float changeFee;
    private Float refundFee;
    private AirlineDTO airline;
    private List<SeatReservationDTO> seatReservations;
}