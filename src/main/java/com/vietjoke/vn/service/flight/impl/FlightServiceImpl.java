package com.vietjoke.vn.service.flight.impl;

import com.vietjoke.vn.config.seeding.jsonObject.Flight;
import com.vietjoke.vn.converter.FareClassConverter;
import com.vietjoke.vn.converter.FlightConverter;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.*;
import com.vietjoke.vn.dto.response.flight.FlightResponseDTO;
import com.vietjoke.vn.dto.response.flight.SearchFlightResponseDTO;
import com.vietjoke.vn.dto.response.flight.SelectFlightResponseDTO;
import com.vietjoke.vn.dto.response.pricing.FlightServiceResponseDTO;
import com.vietjoke.vn.dto.response.pricing.SeatResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import com.vietjoke.vn.exception.flight.FlightNotFoundException;
import com.vietjoke.vn.exception.flight.InvalidTripSelectionException;
import com.vietjoke.vn.repository.flight.FlightRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.fleet.AircraftService;
import com.vietjoke.vn.service.fleet.AirlineService;
import com.vietjoke.vn.service.fleet.RouteService;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.flight.FlightStatusService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
import com.vietjoke.vn.service.pricing.FareAvailabilityService;
import com.vietjoke.vn.service.pricing.FareClassService;
import com.vietjoke.vn.service.pricing.SeatReservationService;
import com.vietjoke.vn.util.enums.flight.TripType;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final AirlineService airlineService;
    private final RouteService routeService;
    private final FlightStatusService flightStatusService;
    private final AircraftService aircraftService;
    private final SeatReservationService seatReservationService;
    private final FareAvailabilityService fareAvailabilityService;
    private final FareClassService fareClassService;

    private final BookingSessionService bookingSessionService;

    private final FlightRepository flightRepository;

    private final FlightConverter flightConverter;
    private final FareClassConverter fareClassConverter;

    @Override
    public FlightEntity getFlightByFlightNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("Flight " + flightNumber + " not found"));
    }

    @Override
    @Transactional
    public void createOrUpdateFlight(Flight flight) {
        FlightEntity flightEntity = new FlightEntity();

        flightEntity.setAirlineEntity(airlineService.getAirlineEntity(flight.getAirlineCode()));

        flightEntity.setRouteEntity(routeService.getRouteEntity(flight.getRouteCode()));

        flightEntity.setScheduledDeparture(flight.getScheduledDeparture());
        flightEntity.setScheduledArrival(flight.getScheduledArrival());
        flightEntity.setGate(flight.getGate());
        flightEntity.setTerminal(flight.getTerminal());

        if (flight.getStatusCode() != null) {
            flightEntity.setFlightStatusEntity(flightStatusService.getFlightStatusEntity(flight.getStatusCode()));
        }

        if (flight.getRegisterNumber() != null) {
            flightEntity.setAircraftEntity(aircraftService.getAircraftEntity(flight.getRegisterNumber()));
        }

        List<SeatReservationEntity> seatReservationEntities = seatReservationService
                .createSeatReservations(flightEntity);

        flightEntity.setSeatReservationEntities(seatReservationEntities);

        flightRepository.save(flightEntity);

        List<FareAvailabilityEntity> fareAvailabilityEntities = fareAvailabilityService
                .createFareAvailability(flight.getFareAvailability(), flightEntity);

        flightEntity.setFareAvailabilityEntities(fareAvailabilityEntities);

        flightRepository.save(flightEntity);
    }

    @Override
    @Transactional
    public ResponseDTO<SearchFlightResponseDTO> searchFlight(SearchParamDTO searchParam) {

        int neededSeats = searchParam.getTripPassengersChild() + searchParam.getTripPassengersAdult();

        Map<String, List<FlightResponseDTO>> routeMap = new LinkedHashMap<>();

        String outbound = searchParam.getTripFrom() + '-' + searchParam.getTripTo();
        List<FlightResponseDTO> outboundFlights = toFlightResponseDTO(outbound, searchParam.getTripStartDate(), neededSeats);
        if(outboundFlights.isEmpty()){
            throw new FlightNotFoundException("Flight " + outbound + " not found");
        }
        routeMap.put(outbound, outboundFlights);

        if(Objects.equals(searchParam.getTripType(), TripType.ROUND_TRIP.getValue())){
            String inbound = searchParam.getTripTo() + '-' + searchParam.getTripFrom();
            List<FlightResponseDTO> inboundFlights = toFlightResponseDTO(inbound, searchParam.getTripReturnDate(), neededSeats);
            if(inboundFlights.isEmpty()){
                throw new FlightNotFoundException("Flight " + inbound + " not found");
            }
            routeMap.put(inbound, inboundFlights);
        }

        BookingSession session = bookingSessionService.createSession(searchParam);
        log.info("Booking Session: {}", session);

        SearchFlightResponseDTO result = SearchFlightResponseDTO.builder()
                .travelOptions(List.of(routeMap))
                .sessionToken(session.getSessionId())
                .expireAt(session.getExpireAt())
                .currentStep(session.getCurrentStep())
                .nextStep(session.getNextStep())
                .build();

        return ResponseDTO.success(result);
    }

    @Override
    @Transactional
    public ResponseDTO<?> selectFlight(SelectFlightRequestDTO selectParam) {

        BookingSession session = bookingSessionService.getSession(selectParam.getSessionToken());
        BookingSessionHelper.validateSelectFlightsSteps(session);
        SearchParamDTO searchParam = session.getSearchCriteria();

        if(Objects.equals(searchParam.getTripType(), TripType.ONEWAY.getValue())){
            if(selectParam.getFlights().size() > 1){
                throw new InvalidTripSelectionException("Multiple flights found");
            }
        }

        int neededSeats = searchParam.getTripPassengersAdult() + searchParam.getTripPassengersChild();

        for(SelectFlightDTO selectFlight : selectParam.getFlights()) {
            FlightEntity flightEntity = this.getFlightByFlightNumber(selectFlight.getFlightNumber());
            FareClassEntity fareClassEntity = fareClassService.getFareClass(selectFlight.getFareCode());
            FareAvailabilityEntity fareAvailabilityEntity = fareAvailabilityService.getFareAvailability(
                    flightEntity,
                    fareClassEntity
            );

            if (neededSeats > fareAvailabilityEntity.getAvailableSeats()) {
                return ResponseDTO.error(400, "You are not enough seats available please return to flight search page");
            }
        }

        session = bookingSessionService.updateSelectedFlight(session.getSessionId(), selectParam);

        SelectFlightResponseDTO selectFlight = SelectFlightResponseDTO.builder()
                .sessionToken(session.getSessionId())
                .expireAt(session.getExpireAt())
                .tripPassengersAdult(searchParam.getTripPassengersAdult())
                .tripPassengersChildren(searchParam.getTripPassengersChild())
                .tripPassengersInfant(searchParam.getTripPassengersInfant())
                .currentStep(session.getCurrentStep())
                .nextStep(session.getNextStep())
                .build();

        return ResponseDTO.success(selectFlight);
    }

    @Override
    public ResponseDTO<?> getSeatOfFlight(SessionTokenRequestDTO sessionToken) {
        BookingSession session = bookingSessionService.getSession(sessionToken.getSessionToken());
        BookingSessionHelper.validateServiceBookingSteps(session);

        SelectFlightRequestDTO selectFlightRequestDTO = session.getSelectedFlight();
        SearchParamDTO searchParamDTO = session.getSearchCriteria();
        List<SelectFlightDTO> selectFlightDTOs = selectFlightRequestDTO.getFlights();

        List<FlightResponseDTO> flightResponseDTOs = new ArrayList<>();

        for(SelectFlightDTO selectFlight : selectFlightDTOs){
            FlightEntity flightEntity = this.getFlightByFlightNumber(selectFlight.getFlightNumber());

            List<SeatResponseDTO> seats = seatReservationService.getSeats(
                    selectFlight.getFlightNumber(),
                    selectFlight.getFareCode()
            );

            int seatCount = (int)seats.stream()
                            .filter(seat -> SeatStatus.AVAILABLE.equals(seat.getSeatStatus()))
                                    .count();

            if(searchParamDTO.getTripPassengers() > seatCount){
                throw new InvalidTripSelectionException("Not enough seats available for the number of passengers");
            }

            FlightResponseDTO flightResponseDTO = flightConverter.convertToResponseDTO(flightEntity);
            flightResponseDTO.setFlightSeats(seats);
            flightResponseDTOs.add(flightResponseDTO);
        }

        FlightServiceResponseDTO flightSeat = FlightServiceResponseDTO.builder()
                .sessionToken(session.getSessionId())
                .flight(flightResponseDTOs)
                .build();

        return ResponseDTO.success(flightSeat);
    }

    private List<FlightResponseDTO> toFlightResponseDTO(String tripType, LocalDate date, int neededSeats) {
        List<FlightEntity> flightEntities = flightRepository.findFlightsBySearchParam(tripType, date);
        return flightEntities.stream()
                .filter(flightEntity -> flightEntity.getFlightStatusEntity().getStatusCode().equals("SCH"))
                .map(flightEntity -> {

                    FlightResponseDTO flightResponseDTO = flightConverter.convertToResponseDTO(flightEntity);

                    List<FareClassDTO> fareClassDTOS = flightEntity.getFareAvailabilityEntities().stream()
                            .map(fareAvailabilityEntity -> {
                                FareClassDTO fareClassDTO = fareClassConverter.convertToDTO(fareAvailabilityEntity);
                                fareClassDTO.setNotEnoughSeats(fareAvailabilityEntity.getAvailableSeats() < neededSeats);
                                return fareClassDTO;
                            })
                            .collect(Collectors.toList());

                    flightResponseDTO.setFareClasses(fareClassDTOS);
                    return flightResponseDTO;
                })
                .collect(Collectors.toList());
    }
}
