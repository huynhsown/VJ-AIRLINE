package com.vietjoke.vn.service.flight.impl;

import com.vietjoke.vn.config.seeding.jsonObject.Flight;
import com.vietjoke.vn.converter.FareClassConverter;
import com.vietjoke.vn.converter.FlightConverter;
import com.vietjoke.vn.converter.RouteConverter;
import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.response.FareClassDTO;
import com.vietjoke.vn.dto.response.FlightResponseDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.SearchFlightResponseDTO;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import com.vietjoke.vn.repository.flight.FlightRepository;
import com.vietjoke.vn.service.fleet.AircraftService;
import com.vietjoke.vn.service.fleet.AirlineService;
import com.vietjoke.vn.service.fleet.RouteService;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.flight.FlightStatusService;
import com.vietjoke.vn.service.pricing.FareAvailabilityService;
import com.vietjoke.vn.service.pricing.SeatReservationService;
import com.vietjoke.vn.util.enums.flight.TripType;
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

    private final FlightRepository flightRepository;

    private final FlightConverter flightConverter;
    private final FareClassConverter fareClassConverter;
    private final RouteConverter routeConverter;

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
            return ResponseDTO.error(404, "No flight found");
        }
        routeMap.put(outbound, outboundFlights);

        if(Objects.equals(searchParam.getTripType(), TripType.ROUND_TRIP.getValue())){
            String inbound = searchParam.getTripTo() + '-' + searchParam.getTripFrom();
            List<FlightResponseDTO> inboundFlights = toFlightResponseDTO(inbound, searchParam.getTripReturnDate(), neededSeats);
            if(inboundFlights.isEmpty()){
                return ResponseDTO.error(404, "No flight found");
            }
            routeMap.put(inbound, inboundFlights);
        }

        SearchFlightResponseDTO result = SearchFlightResponseDTO.builder()
                .travelOptions(List.of(routeMap)).build();

        return ResponseDTO.success(result);
    }

    private List<FlightResponseDTO> toFlightResponseDTO(String tripType, LocalDate date, int neededSeats) {
        List<FlightEntity> flightEntities = flightRepository.findFlightsBySearchParam(tripType, date);
        return flightEntities.stream()
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
