package com.vietjoke.vn.dto.pricing;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PromoDTO {
    private String code;
    private String description;
    private BigDecimal amount;
    private Boolean isPercentage;
    private Boolean isAvailable;
}
