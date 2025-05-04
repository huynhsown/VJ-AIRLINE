package com.vietjoke.vn.dto.booking;

import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.flight.FlightDTO;
import com.vietjoke.vn.util.enums.user.PassengerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookingDetailDTO extends BaseDTO {
    private FlightDTO flight;
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

    private String passengerFirstName;
    private String passengerLastName;
    private LocalDate passengerDateOfBirth;
    private String passengerIdNumber;
    private PassengerType passengerType;

    private String fareClassName;
}