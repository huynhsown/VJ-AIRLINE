package com.vietjoke.vn.dto.response.pricing;

import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import lombok.Data;

@Data
public class SeatResponseDTO {
    private Long id;
    private String seatNumber;
    private String flightNumber;
    private String fareCode;
    private SeatStatus seatStatus;
}
