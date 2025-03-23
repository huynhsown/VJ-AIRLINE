package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookingDetailDTO extends BaseDTO {
    private Long bookingId;
    private Long flightId;
    private Long passengerId;
    private Long fareClassId;
    private String seatNumber;
    private BigDecimal fareAmount;
    private BigDecimal taxAmount;
    private BigDecimal feeAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private boolean checkedIn;
    private LocalDateTime checkinTime;
    private List<BookingAddonDTO> bookingAddons;
    private Long seatReservationId;
}