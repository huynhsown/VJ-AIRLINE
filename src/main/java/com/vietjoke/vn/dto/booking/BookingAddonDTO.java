package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookingAddonDTO extends BaseDTO {
    private Long bookingDetailId;
    private Long addonId;
    private BigDecimal price;
    private Integer quantity;
}