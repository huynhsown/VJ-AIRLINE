package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.booking.BookingDetailedViewDTO;
import com.vietjoke.vn.dto.booking.BookingDetailedViewDTO.AddonDetailDTO;
import com.vietjoke.vn.dto.booking.BookingDetailedViewDTO.FlightInfoDTO;
import com.vietjoke.vn.dto.booking.BookingDetailedViewDTO.PaymentDetailDTO;
import com.vietjoke.vn.dto.booking.BookingDetailedViewDTO.PassengerDetailDTO;
import com.vietjoke.vn.dto.booking.BookingHistoryDTO;
import com.vietjoke.vn.dto.booking.BookingHistoryDTO.FlightSimplifyDTO;
import com.vietjoke.vn.entity.booking.BookingAddonEntity;
import com.vietjoke.vn.entity.booking.BookingDetailEntity;
import com.vietjoke.vn.entity.booking.BookingEntity;
import com.vietjoke.vn.entity.fleet.AircraftEntity;
import com.vietjoke.vn.entity.fleet.AircraftModelEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.entity.user.PassengerEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingConverter {

    private final ModelMapper modelMapper;

    public BookingHistoryDTO toBookingHistoryDTO(BookingEntity bookingEntity) {
        BookingHistoryDTO bookingHistoryDTO = modelMapper.map(bookingEntity, BookingHistoryDTO.class);
        List<BookingDetailEntity> bookingDetailEntities = bookingEntity.getBookingDetailEntities();
        List<FlightEntity> flightEntities = bookingDetailEntities.stream()
                .map(BookingDetailEntity::getFlightEntity)
                .distinct()
                .sorted(Comparator.comparing(FlightEntity::getScheduledDeparture))
                .toList();
        List<FlightSimplifyDTO> flights = flightEntities.stream()
                .map(i -> modelMapper.map(i, FlightSimplifyDTO.class))
                .toList();
        bookingHistoryDTO.setFlights(flights);
        return bookingHistoryDTO;
    }

    public BookingDetailedViewDTO toBookingDetailedViewDTO(BookingEntity bookingEntity) {
        BookingDetailedViewDTO bookingDetailedView = modelMapper.map(bookingEntity, BookingDetailedViewDTO.class);

        Map<PassengerEntity, List<BookingDetailEntity>> passengerDetailsMap = bookingEntity.getBookingDetailEntities()
                .stream()
                .collect(Collectors.groupingBy(BookingDetailEntity::getPassengerEntity));

        List<PassengerDetailDTO> passengerDetails = passengerDetailsMap.entrySet().stream()
                .map(entry -> {
                    PassengerEntity passenger = entry.getKey();
                    List<BookingDetailEntity> details = entry.getValue();
                    PassengerDetailDTO passengerDTO = modelMapper.map(passenger, PassengerDetailDTO.class);
                    List<FlightInfoDTO> flights = details.stream()
                            .map(detail -> {
                                FlightInfoDTO flightInfoDTO = modelMapper.map(detail, FlightInfoDTO.class);
                                FlightEntity flightEntity = detail.getFlightEntity();
                                AircraftEntity aircraftEntity = flightEntity.getAircraftEntity();
                                AircraftModelEntity modelEntity = aircraftEntity.getAircraftModelEntity();
                                String departureAirport = flightEntity.getRouteEntity().getOriginAirportEntity().getAirportName();
                                String arrivalAirport = flightEntity.getRouteEntity().getDestinationAirportEntity().getAirportName();
                                String airlineName = flightEntity.getAirlineEntity().getAirlineName();
                                flightInfoDTO.setFlightDepartureAirport(departureAirport);
                                flightInfoDTO.setFlightArrivalAirport(arrivalAirport);
                                flightInfoDTO.setAirlineName(airlineName);
                                flightInfoDTO.setAircraftRegistrationNumber(aircraftEntity.getRegistrationNumber());
                                flightInfoDTO.setAircraftModelCode(modelEntity.getModelCode());
                                flightInfoDTO.setAddons(mapAddons(detail.getBookingAddonEntities()));

                                return flightInfoDTO;
                            })
                            .toList();
                    passengerDTO.setFlights(flights);
                    return passengerDTO;
                })
                .toList();
        bookingDetailedView.setPassengers(passengerDetails);

        List<PaymentDetailDTO> paymentDetails = bookingEntity.getBookingPaymentEntities().stream()
                .map(i -> modelMapper.map(i, PaymentDetailDTO.class))
                .toList();
        bookingDetailedView.setPayments(paymentDetails);

        return bookingDetailedView;
    }

    private List<AddonDetailDTO> mapAddons(List<BookingAddonEntity> addonEntities) {
        if (addonEntities == null) return new ArrayList<>();

        return addonEntities.stream()
                .map(addon -> modelMapper.map(addon, AddonDetailDTO.class))
                .toList();
    }

}
