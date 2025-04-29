package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.booking.BookingSessionDTO;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.response.user.PassengerInfoResponseDTO;
import com.vietjoke.vn.dto.response.user.SimplifiedPassengerDTO;
import com.vietjoke.vn.dto.user.PassengerDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingSessionConverter {

    public BookingSessionDTO toBookingSessionDTO(BookingSession bookingSession) {
        BookingSessionDTO bookingSessionDTO = new BookingSessionDTO();
        bookingSessionDTO.setSessionId(bookingSession.getSessionId());
        bookingSessionDTO.setSearchCriteria(bookingSession.getSearchCriteria());
        bookingSessionDTO.setSelectedFlight(bookingSession.getSelectedFlight());
        PassengersInfoParamDTO passengersInfoParamDTO = bookingSession.getPassengersInfoParamDTO();
        List<PassengerDTO> allPassengers = new ArrayList<>();
        allPassengers.addAll(passengersInfoParamDTO.getPassengersAdult());
        allPassengers.addAll(passengersInfoParamDTO.getPassengersChild());
        allPassengers.addAll(passengersInfoParamDTO.getPassengersInfant());

        List<SimplifiedPassengerDTO> simplifiedPassengers = allPassengers.stream()
                .map(passenger -> SimplifiedPassengerDTO.builder()
                        .uuid(passenger.getUuid())
                        .firstName(passenger.getFirstName())
                        .lastName(passenger.getLastName())
                        .build())
                .toList();

        bookingSessionDTO.setPassengerInfo(PassengerInfoResponseDTO.builder()
                .passengers(simplifiedPassengers)
                .build());
        return bookingSessionDTO;
    }

}
