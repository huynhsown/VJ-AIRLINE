package com.vietjoke.vn.service.user.impl;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.booking.SelectFlightParamDTO;
import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.user.PassengerInfoResponseDTO;
import com.vietjoke.vn.dto.response.user.SimplifiedPassengerDTO;
import com.vietjoke.vn.dto.user.PassengerDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.booking.MissingBookingStepException;
import com.vietjoke.vn.exception.user.PassengerInfomationException;
import com.vietjoke.vn.exception.user.PassengerTypeRequiredException;
import com.vietjoke.vn.repository.user.PassengerRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.user.PassengerService;
import com.vietjoke.vn.util.enums.user.PassengerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final BookingSessionService bookingSessionService;

    private final PassengerRepository passengerRepository;

    @Override
    public ResponseDTO<Map<String, String>> inputPassengerInfo(PassengersInfoParamDTO infoParamDTO) {
        BookingSession session = bookingSessionService.getSession(infoParamDTO.getBookingSession());
        SelectFlightRequestDTO selectFlightParamDTO = session.getSelectedFlight();
        SearchParamDTO searchParamDTO = session.getSearchCriteria();
        if (selectFlightParamDTO == null) {
            throw new MissingBookingStepException("Flight selection step is missing");
        }

        List<PassengerDTO> allPassengers = new ArrayList<PassengerDTO>();
        allPassengers.addAll(infoParamDTO.getPassengersAdult());
        allPassengers.addAll(infoParamDTO.getPassengersChild());
        allPassengers.addAll(infoParamDTO.getPassengersInfant());

        validatePassengers(allPassengers);

        if (searchParamDTO.getTripPassengersAdult() != infoParamDTO.getPassengersAdult().size()
                || searchParamDTO.getTripPassengersChild() != infoParamDTO.getPassengersChild().size()
                || searchParamDTO.getTripPassengersInfant() != infoParamDTO.getPassengersInfant().size()) {
            throw new IllegalArgumentException("Number of passengers doesn't match the initial search criteria");
        }

        session = bookingSessionService.updatePassengerInfo(session.getSessionId(), infoParamDTO);

        return ResponseDTO.success(Map.of("sessionToken", session.getSessionId()));
    }

    @Override
    public PassengerInfoResponseDTO getPassengerInfo(SessionTokenRequestDTO sessionToken) {
        BookingSession session = bookingSessionService.getSession(sessionToken.getSessionToken());
        PassengersInfoParamDTO passengersInfoParamDTO = session.getPassengersInfoParamDTO();
        if (passengersInfoParamDTO == null) {
            throw new MissingBookingStepException("Passengers info step is missing");
        }
        List<PassengerDTO> allPassengers = new ArrayList<PassengerDTO>();
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

        return PassengerInfoResponseDTO.builder()
                .sessionToken(session.getSessionId())
                .passengers(simplifiedPassengers)
                .build();
    }

    private void validatePassengers(List<PassengerDTO> passengers) {
        if (passengers == null) return;

        passengers.forEach(this::validatePassenger);
        validateInfantPassenger(passengers);
    }

    private void validatePassenger(PassengerDTO passenger) {
        if (passenger.getPassengerType() == null) {
            throw new PassengerTypeRequiredException("Passenger type is required");
        }

        if (passenger.getPassengerType() == PassengerType.ADULT) {
            validateAdultPassenger(passenger);
        }
    }

    private void validateAdultPassenger(PassengerDTO passenger) {
        if (isBlank(passenger.getCountryCode()) ||
                passenger.getIdType() == null ||
                isBlank(passenger.getIdNumber()) ||
                isBlank(passenger.getPhone()) ||
                isBlank(passenger.getEmail())) {

            throw new PassengerInfomationException("Missing passenger information");
        }
    }

    private void validateInfantPassenger(List<PassengerDTO> passengers) {
        Set<String> adultsNames = passengers.stream()
                .filter(passenger -> passenger.getPassengerType() == PassengerType.ADULT)
                .map(passenger -> passenger.getFirstName() + ' ' + passenger.getLastName())
                .collect(Collectors.toSet());

        for (PassengerDTO passenger : passengers) {
            if (passenger.getPassengerType() == PassengerType.INFANT
                    && (passenger.getAccompanyingAdultFirstName() == null
                    || passenger.getAccompanyingAdultLastName() == null
                    || !adultsNames.contains(passenger.getAccompanyingAdultFirstName()
                        + ' '
                        + passenger.getAccompanyingAdultLastName())
            )) {
                throw new PassengerInfomationException("Missing passenger information");
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
