package com.vietjoke.vn.service.helper;

import com.vietjoke.vn.converter.BookingPreviewConverter;
import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.service.pricing.FareAvailabilityService;
import com.vietjoke.vn.service.pricing.FareClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FlightProcessor {
    private final FlightService flightService;
    private final FareClassService fareClassService;
    private final FareAvailabilityService fareAvailabilityService;
    private final AddonService addonService;
    private final BookingPreviewConverter bookingPreviewConverter;

    public List<BookingPreviewDTO.FlightSummaryDTO> processFlights(
            SelectFlightRequestDTO selectedFlight,
            List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerDetails) {

        return selectedFlight.getFlights().stream()
                .map(SelectFlightDTO::getFlightNumber)
                .distinct()
                .map(flightNumber -> createFlightSummary(flightNumber, passengerDetails))
                .toList();
    }

    private BookingPreviewDTO.FlightSummaryDTO createFlightSummary(
            String flightNumber,
            List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerDetails) {

        List<BookingPreviewDTO.PassengerFlightDetailDTO> matchingFlights = findMatchingFlights(
                flightNumber, passengerDetails);

        return BookingPreviewDTO.FlightSummaryDTO.builder()
                .flightNumber(flightNumber)
                .totalTicketPrice(calculateTotalTicketPrice(matchingFlights))
                .totalAddonPrice(calculateTotalAddonPrice(matchingFlights))
                .build();
    }

    private List<BookingPreviewDTO.PassengerFlightDetailDTO> findMatchingFlights(
            String flightNumber,
            List<BookingPreviewDTO.PassengerBookingDetailDTO> passengerDetails) {

        return passengerDetails.stream()
                .flatMap(p -> p.getPassengerFlightDetailDTOS().stream())
                .filter(f -> flightNumber.equals(f.getFlightNumber()))
                .toList();
    }

    private BigDecimal calculateTotalTicketPrice(List<BookingPreviewDTO.PassengerFlightDetailDTO> flights) {
        return flights.stream()
                .map(f -> Optional.ofNullable(f.getTicketPrice()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalAddonPrice(List<BookingPreviewDTO.PassengerFlightDetailDTO> flights) {
        return flights.stream()
                .map(f -> Optional.ofNullable(f.getAddonPrice()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
