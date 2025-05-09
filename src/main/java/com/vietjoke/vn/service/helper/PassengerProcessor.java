package com.vietjoke.vn.service.helper;

import com.vietjoke.vn.converter.BookingPreviewConverter;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.dto.user.PassengerDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.service.pricing.FareAvailabilityService;
import com.vietjoke.vn.service.pricing.FareClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PassengerProcessor {
    private final FlightService flightService;
    private final FareClassService fareClassService;
    private final FareAvailabilityService fareAvailabilityService;
    private final AddonService addonService;
    private final BookingPreviewConverter bookingPreviewConverter;

    public List<BookingPreviewDTO.PassengerBookingDetailDTO> processPassengers(
            BookingSession session) {

        PassengersInfoParamDTO infoParamDTO = session.getPassengersInfoParamDTO();
        List<PassengerDTO> passengers = BookingSessionHelper.getAllPassengers(infoParamDTO);
        List<SelectFlightDTO> selectedFlights = session.getSelectedFlight().getFlights();

        Map<String, FlightEntity> flightCache = createFlightCache(selectedFlights);
        Map<String, FareClassEntity> fareClassCache = createFareClassCache(selectedFlights);
        Map<String, FareAvailabilityEntity> fareAvailabilityCache = createFareAvailabilityCache(
                selectedFlights, flightCache, fareClassCache);

        return passengers.stream()
                .map(passenger -> createPassengerBookingDetail(
                        passenger,
                        selectedFlights,
                        session.getPassengerAddons(),
                        flightCache,
                        fareAvailabilityCache))
                .toList();
    }

    private Map<String, FlightEntity> createFlightCache(List<SelectFlightDTO> flights) {
        return flights.stream()
                .collect(Collectors.toMap(
                        SelectFlightDTO::getFlightNumber,
                        flight -> flightService.getFlightByFlightNumber(flight.getFlightNumber()),
                        (existing, replacement) -> existing));
    }

    private Map<String, FareAvailabilityEntity> createFareAvailabilityCache(
            List<SelectFlightDTO> flights,
            Map<String, FlightEntity> flightCache,
            Map<String, FareClassEntity> fareClassCache) {

        return flights.stream()
                .collect(Collectors.toMap(
                        flight -> flight.getFlightNumber() + "_" + flight.getFareCode(),
                        flight -> {
                            FlightEntity flightEntity = flightCache.get(flight.getFlightNumber());
                            FareClassEntity fareClassEntity = fareClassCache.get(flight.getFareCode());
                            return fareAvailabilityService.getFareAvailability(flightEntity, fareClassEntity);
                        },
                        (existing, replacement) -> existing));
    }

    private Map<String, FareClassEntity> createFareClassCache(List<SelectFlightDTO> flights) {
        return flights.stream()
                .collect(Collectors.toMap(
                        SelectFlightDTO::getFareCode,
                        flight -> fareClassService.getFareClass(flight.getFareCode()),
                        (existing, replacement) -> existing));
    }

    private BookingPreviewDTO.PassengerBookingDetailDTO createPassengerBookingDetail(
            PassengerDTO passenger,
            List<SelectFlightDTO> selectedFlights,
            Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons,
            Map<String, FlightEntity> flightCache,
            Map<String, FareAvailabilityEntity> fareAvailabilityCache) {

        List<BookingPreviewDTO.PassengerFlightDetailDTO> flightDetails = selectedFlights.stream()
                .map(flight -> createPassengerFlightDetail(
                        passenger,
                        flight,
                        passengerAddons,
                        flightCache,
                        fareAvailabilityCache))
                .toList();

        return bookingPreviewConverter.toPassengerBookingDetailDTO(passenger, flightDetails);
    }

    private BookingPreviewDTO.PassengerFlightDetailDTO createPassengerFlightDetail(
            PassengerDTO passenger,
            SelectFlightDTO flight,
            Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons,
            Map<String, FlightEntity> flightCache,
            Map<String, FareAvailabilityEntity> fareAvailabilityCache) {

        String flightNumber = flight.getFlightNumber();
        String fareCode = flight.getFareCode();
        String cacheKey = flightNumber + "_" + fareCode;

        FlightEntity flightEntity = flightCache.get(flightNumber);
        FareAvailabilityEntity fareAvailability = fareAvailabilityCache.get(cacheKey);
        List<AddonSelectionDTO> addons = getPassengerAddons(passenger, flightNumber, passengerAddons);
        List<BookingPreviewDTO.AddonDetailDTO> addonDetails = processAddons(addons);

        return bookingPreviewConverter.toPassengerFlightDetailDTO(
                flightEntity,
                fareAvailability,
                addonDetails);
    }

    private List<AddonSelectionDTO> getPassengerAddons(
            PassengerDTO passenger,
            String flightNumber,
            Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons) {

        return Optional.ofNullable(passengerAddons)
                .map(map -> map.get(passenger.getUuid()))
                .map(flightMap -> flightMap.get(flightNumber))
                .orElse(Collections.emptyList());
    }

    private List<BookingPreviewDTO.AddonDetailDTO> processAddons(List<AddonSelectionDTO> addons) {
        return addons.stream()
                .map(addon -> {
                    AddonEntity addonEntity = addonService.getAddonById(addon.getAddonId());
                    return bookingPreviewConverter.toAddonDetailDTO(addonEntity, addon);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
