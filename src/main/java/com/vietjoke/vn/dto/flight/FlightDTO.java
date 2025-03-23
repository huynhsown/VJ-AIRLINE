package com.vietjoke.vn.dto.flight;

import com.vietjoke.vn.dto.booking.BookingDetailDTO;
import com.vietjoke.vn.dto.core.BaseDTO;
import com.vietjoke.vn.dto.fleet.AircraftDTO;
import com.vietjoke.vn.dto.fleet.AirlineDTO;
import com.vietjoke.vn.dto.fleet.RouteDTO;
import com.vietjoke.vn.dto.pricing.SeatReservationDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlightDTO extends BaseDTO {
    private String flightNumber;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private String gate;
    private String terminal;
    private AirlineDTO airline;
    private RouteDTO route;
    private AircraftDTO aircraft;
    private FlightStatusDTO flightStatus;
    private List<BookingDetailDTO> bookingDetails;
    private List<SeatReservationDTO> seatReservations;
}