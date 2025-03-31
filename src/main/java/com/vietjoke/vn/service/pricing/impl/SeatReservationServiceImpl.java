package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.entity.fleet.AircraftEntity;
import com.vietjoke.vn.entity.fleet.AircraftModelEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import com.vietjoke.vn.repository.pricing.SeatReservationRepository;
import com.vietjoke.vn.service.pricing.FareClassService;
import com.vietjoke.vn.service.pricing.SeatReservationService;
import com.vietjoke.vn.util.enums.pricing.FareClassCode;
import com.vietjoke.vn.util.enums.pricing.SeatStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SeatReservationServiceImpl implements SeatReservationService {

    private final SeatReservationRepository seatReservationRepository;
    private final FareClassService fareClassService;

    @Override
    @Transactional
    public List<SeatReservationEntity> createSeatReservations(FlightEntity flightEntity) {
        AircraftModelEntity aircraftModelEntity = getAircraftModelEntity(flightEntity);

        List<SeatReservationEntity> seatReservationEntities = new ArrayList<>();

        Map<FareClassCode, Integer> capacityMap = Map.of(
                FareClassCode.PRM, aircraftModelEntity.getPremiumCapacity(),
                FareClassCode.BUS, aircraftModelEntity.getBusinessCapacity(),
                FareClassCode.STD, aircraftModelEntity.getStandardCapacity()
        );

        for (FareClassCode code : capacityMap.keySet()) {
            FareClassEntity fareClass = fareClassService.getFareClass(code.name());
            addSeats(seatReservationEntities, flightEntity, fareClass, capacityMap.get(code), code);
        }

        return seatReservationRepository.saveAll(seatReservationEntities);
    }

    @Override
    public Integer countAvailableSeat(FlightEntity flightEntity, FareClassEntity fareClassEntity) {
        return seatReservationRepository.countBySeatStatusAndFlightEntityAndFareClassEntity(SeatStatus.AVAILABLE, flightEntity, fareClassEntity);
    }

    private static AircraftModelEntity getAircraftModelEntity(FlightEntity flightEntity) {
        AircraftEntity aircraftEntity = flightEntity.getAircraftEntity();
        if (aircraftEntity == null) {
            throw new IllegalArgumentException("AircraftEntity cannot be null for flight: " + flightEntity.getId());
        }
        AircraftModelEntity aircraftModelEntity = aircraftEntity.getAircraftModelEntity();
        if (aircraftModelEntity == null) {
            throw new IllegalArgumentException("AircraftModelEntity cannot be null for aircraft: " + aircraftEntity.getId());
        }
        return aircraftModelEntity;
    }

    private void addSeats(List<SeatReservationEntity> seats, FlightEntity flight, FareClassEntity fareClass,
                          int capacity, FareClassCode code) {
        for (int i = 1; i <= capacity; i++) {
            SeatReservationEntity seat = SeatReservationEntity.builder()
                    .seatNumber(code.name() + "-" + i)
                    .seatStatus(SeatStatus.AVAILABLE)
                    .fareClassEntity(fareClass)
                    .flightEntity(flight)
                    .build();
            seats.add(seat);
        }
    }
}