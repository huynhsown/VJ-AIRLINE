package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.dto.booking.BookingDetailDTO;
import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.flight.FlightDTO;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SeatReservationDTO extends BaseDTO {
    private String seatNumber;
    private SeatStatus seatStatus;
    private FlightDTO flight;
    private FareClassDTO fareClass;
    private BookingDetailDTO bookingDetail;
}