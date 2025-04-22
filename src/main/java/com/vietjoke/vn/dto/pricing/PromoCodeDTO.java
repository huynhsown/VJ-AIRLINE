package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PromoCodeDTO extends BaseDTO {
    private String code;
    private String description;
    private BigDecimal amount;
    private Boolean isPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer maxUsage;
    private Integer currentUsage;
    private Boolean isActive;
    private BigDecimal minBookingAmount;
    private Integer minPassengers;
    private Boolean isFirstTime;
}