package com.vietjoke.vn.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FareClassDTO {
    private String fareClassCode;
    private String fareClassName;
    private Integer availableSeats;
    private BigDecimal basePrice;
    private boolean isNotEnoughSeats;
}
