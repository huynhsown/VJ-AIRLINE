package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.util.enums.pricing.PaymentMethodCode;
import lombok.Data;

@Data
public class PaymentMethodDTO {
    private PaymentMethodCode code;
    private String name;
    private String logoUrl;
    private Boolean enabled;
}
