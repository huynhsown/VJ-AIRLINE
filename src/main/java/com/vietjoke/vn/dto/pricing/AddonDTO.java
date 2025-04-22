package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.dto.booking.BookingAddonDTO;
import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import com.vietjoke.vn.util.enums.pricing.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddonDTO extends BaseDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private Boolean isActive = true;
    private String addonTypeCode;
    private String imgUrl;
}