package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookingStatusDTO extends BaseDTO {
    private BookingStatus statusCode;
    private String statusName;
    private String description;
}