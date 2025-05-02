package com.vietjoke.vn.service.helper;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.user.PassengerDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.booking.MissingBookingStepException;
import com.vietjoke.vn.util.enums.booking.BookingSessionStep;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for validating and extracting information from {@link BookingSession}.
 * Contains methods to ensure that all booking steps are completed correctly.
 */
public final class BookingSessionHelper {

    private BookingSessionHelper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Checks if a booking session is valid and not expired.
     *
     * @param session the booking session to check
     * @return true if the session exists and has not expired, false otherwise
     */
    public static boolean isValidSession(BookingSession session) {
        if (session == null) {
            return false;
        }
        LocalDateTime expireAt = session.getExpireAt();
        return expireAt != null && expireAt.isAfter(LocalDateTime.now());
    }

    /**
     * Ensures the booking session is valid and not expired.
     *
     * @param session the booking session to validate
     * @throws MissingBookingStepException if the session is null or expired
     */
    public static void requireValidSession(BookingSession session) {
        if (session == null) {
            throw new MissingBookingStepException("Booking session does not exist.");
        }
        LocalDateTime expireAt = session.getExpireAt();
        if (expireAt == null || expireAt.isBefore(LocalDateTime.now())) {
            throw new MissingBookingStepException("Booking session has expired or is invalid.");
        }
    }

    /**
     * Retrieves the selected flight request DTO from the session and ensures it exists.
     *
     * @param session the booking session
     * @return the selected flight request DTO
     * @throws MissingBookingStepException if the session or selected flight is missing
     */
    public static SelectFlightRequestDTO requireSelectedFlight(BookingSession session) {
        if (session == null) {
            throw new MissingBookingStepException("Booking session does not exist.");
        }
        SelectFlightRequestDTO selected = session.getSelectedFlight();
        if (selected == null || selected.getFlights() == null || selected.getFlights().isEmpty()) {
            throw new MissingBookingStepException("Flight selection step is missing.");
        }
        return selected;
    }

    /**
     * Ensures the search criteria has been provided in the booking session.
     *
     * @param session the booking session to validate
     * @throws MissingBookingStepException if the search criteria is missing
     */
    public static void requireSearchCriteria(BookingSession session) {
        requireValidSession(session); // đảm bảo session tồn tại và chưa hết hạn
        if (session.getSearchCriteria() == null) {
            throw new MissingBookingStepException("Chưa thực hiện bước tìm kiếm chuyến bay.");
        }
    }


    /**
     * Retrieves the passengers info DTO from the session and ensures it exists and contains at least one adult.
     *
     * @param session the booking session
     * @return the passengers info DTO
     * @throws MissingBookingStepException if the session or passengers info is missing
     */
    public static PassengersInfoParamDTO requirePassengersInfo(BookingSession session) {
        if (session == null) {
            throw new MissingBookingStepException("Booking session does not exist.");
        }
        PassengersInfoParamDTO info = session.getPassengersInfoParamDTO();
        if (info == null || info.getPassengersAdult() == null || info.getPassengersAdult().isEmpty()) {
            throw new MissingBookingStepException("Passengers info step is missing or no adult passengers found.");
        }
        return info;
    }

    /**
     * Finds a specific flight in the session by its flight number.
     *
     * @param session      the booking session
     * @param flightNumber the flight number
     * @return the matching SelectFlightDTO
     * @throws MissingBookingStepException if the flight is not found
     * @throws IllegalArgumentException    if the flightNumber is null or empty
     */
    public static SelectFlightDTO requireFlight(BookingSession session, String flightNumber) {
        if (!StringUtils.hasText(flightNumber)) {
            throw new IllegalArgumentException("Flight number must not be null or empty.");
        }
        SelectFlightRequestDTO requestDto = requireSelectedFlight(session);
        return requestDto.getFlights().stream()
                .filter(f -> flightNumber.equals(f.getFlightNumber()))
                .findFirst()
                .orElseThrow(() -> new MissingBookingStepException(
                        String.format("Flight with number '%s' not found in session.", flightNumber)
                ));
    }

    /**
     * Aggregates all passenger lists into a single list.
     *
     * @param info the passengers info DTO
     * @return a list of all passengers, or an empty list if info is null
     */
    public static List<PassengerDTO> getAllPassengers(PassengersInfoParamDTO info) {
        if (info == null) {
            return Collections.emptyList();
        }
        return Stream.of(
                        Optional.ofNullable(info.getPassengersAdult()).orElse(Collections.emptyList()),
                        Optional.ofNullable(info.getPassengersChild()).orElse(Collections.emptyList()),
                        Optional.ofNullable(info.getPassengersInfant()).orElse(Collections.emptyList())
                )
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Finds a passenger by UUID in the session.
     *
     * @param session       the booking session
     * @param passengerUuid the passenger UUID
     * @return the matching PassengerDTO
     * @throws MissingBookingStepException if the passenger is not found
     * @throws IllegalArgumentException    if the passengerUuid is null or empty
     */
    public static PassengerDTO requirePassenger(BookingSession session, String passengerUuid) {
        if (!StringUtils.hasText(passengerUuid)) {
            throw new IllegalArgumentException("Passenger UUID must not be null or empty.");
        }
        PassengersInfoParamDTO info = requirePassengersInfo(session);
        return getAllPassengers(info).stream()
                .filter(p -> passengerUuid.equals(p.getUuid()))
                .findFirst()
                .orElseThrow(() -> new MissingBookingStepException(
                        String.format("Passenger with UUID '%s' not found in session.", passengerUuid)
                ));
    }

    public static void validateSeatBookingSteps(BookingSession session,
                                                String flightNumber,
                                                String passengerUuid,
                                                String seatNumber) {
        if (session == null) {
            throw new MissingBookingStepException("Booking session does not exist.");
        }
        if (!StringUtils.hasText(flightNumber)) {
            throw new IllegalArgumentException("Flight number must not be null or empty.");
        }
        if (!StringUtils.hasText(passengerUuid)) {
            throw new IllegalArgumentException("Passenger UUID must not be null or empty.");
        }
        requireValidSession(session);
        if(session.getCurrentStep() == null || session.getCurrentStep() != BookingSessionStep.SELECT_SERVICE){
            throw new MissingBookingStepException("Cannot select service: The booking session is not in the correct state. Expected step is SELECT_SERVICE");
        }

        SelectFlightRequestDTO selectedFlight = requireSelectedFlight(session);
        boolean flightFound = selectedFlight.getFlights().stream()
                .anyMatch(f -> flightNumber.equals(f.getFlightNumber()));
        if (!flightFound) {
            throw new MissingBookingStepException(
                    String.format("Flight with number '%s' not found in session.", flightNumber)
            );
        }

        PassengersInfoParamDTO passengersInfo = requirePassengersInfo(session);
        boolean passengerFound = getAllPassengers(passengersInfo).stream()
                .anyMatch(p -> passengerUuid.equals(p.getUuid()));
        if (!passengerFound) {
            throw new MissingBookingStepException(
                    String.format("Passenger with UUID '%s' not found in session.", passengerUuid)
            );
        }
    }

    public static void validateServiceBookingSteps(BookingSession session, String flightNumber, String passengerUuid) {
        if (session == null) {
            throw new MissingBookingStepException("Booking session does not exist.");
        }
        if (!StringUtils.hasText(flightNumber)) {
            throw new IllegalArgumentException("Flight number must not be null or empty.");
        }
        if (!StringUtils.hasText(passengerUuid)) {
            throw new IllegalArgumentException("Passenger UUID must not be null or empty.");
        }
        requireValidSession(session);
        if(session.getCurrentStep() == null || session.getCurrentStep() != BookingSessionStep.SELECT_SERVICE){
            throw new MissingBookingStepException("Cannot select service: The booking session is not in the correct state. Expected step is SELECT_SERVICE");
        }

        SelectFlightRequestDTO selectedFlight = requireSelectedFlight(session);
        boolean flightFound = selectedFlight.getFlights().stream()
                .anyMatch(f -> flightNumber.equals(f.getFlightNumber()));
        if (!flightFound) {
            throw new MissingBookingStepException(
                    String.format("Flight with number '%s' not found in session.", flightNumber)
            );
        }

        PassengersInfoParamDTO passengersInfo = requirePassengersInfo(session);
        boolean passengerFound = getAllPassengers(passengersInfo).stream()
                .anyMatch(p -> passengerUuid.equals(p.getUuid()));
        if (!passengerFound) {
            throw new MissingBookingStepException(
                    String.format("Passenger with UUID '%s' not found in session.", passengerUuid)
            );
        }
    }

    public static void validateServiceBookingSteps(BookingSession session, String flightNumber) {
        if (session == null) {
            throw new MissingBookingStepException("Booking session does not exist.");
        }
        if (!StringUtils.hasText(flightNumber)) {
            throw new IllegalArgumentException("Flight number must not be null or empty.");
        }
        requireValidSession(session);
        if(session.getCurrentStep() == null || session.getCurrentStep() != BookingSessionStep.SELECT_SERVICE){
            throw new MissingBookingStepException("Cannot select service: The booking session is not in the correct state. Expected step is SELECT_SERVICE");
        }

        SelectFlightRequestDTO selectedFlight = requireSelectedFlight(session);
        boolean flightFound = selectedFlight.getFlights().stream()
                .anyMatch(f -> flightNumber.equals(f.getFlightNumber()));
        if (!flightFound) {
            throw new MissingBookingStepException(
                    String.format("Flight with number '%s' not found in session.", flightNumber)
            );
        }
    }

    public static void validateServiceBookingSteps(BookingSession session) {
        requireSearchCriteria(session);
        requireSelectedFlight(session);
        requirePassengersInfo(session);
        if(session.getCurrentStep() == null || session.getCurrentStep() != BookingSessionStep.SELECT_SERVICE){
            throw new MissingBookingStepException("Cannot select service: The booking session is not in the correct state. Expected step is SELECT_SERVICE");
        }
    }

    public static void validateInputPassengerSteps(BookingSession session) {
        requireSearchCriteria(session);
        requireSelectedFlight(session);
        if(session.getCurrentStep() == null || session.getCurrentStep() != BookingSessionStep.INPUT_PASSENGERS){
            throw new MissingBookingStepException("Cannot input passenger information: The booking session is not in the correct state. Expected step is INPUT_PASSENGERS");
        }
    }

    public static void validateSelectFlightsSteps(BookingSession session) {
        requireSearchCriteria(session);
        if(session.getCurrentStep() == null || session.getCurrentStep() != BookingSessionStep.SELECT_FLIGHT){
            throw new MissingBookingStepException("Cannot select flight: The booking session is not in the correct state. Expected step is SELECT_FLIGHT");
        }
    }
}
