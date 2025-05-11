package com.vietjoke.vn.dto.pricing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayPalCaptureDTO {
    private String captureId;
    private String captureStatus;
    private BigDecimal amount;
    private String currency;
}
