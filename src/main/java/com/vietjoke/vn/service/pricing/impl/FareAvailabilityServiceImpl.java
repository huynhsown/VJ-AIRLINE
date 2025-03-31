package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.config.seeding.jsonObject.FareAvailbility;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.repository.pricing.FareAvailabilityRepository;
import com.vietjoke.vn.service.pricing.FareAvailabilityService;
import com.vietjoke.vn.service.pricing.FareClassService;
import com.vietjoke.vn.service.pricing.SeatReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FareAvailabilityServiceImpl implements FareAvailabilityService {

    private final FareClassService fareClassService;
    private final SeatReservationService seatReservationService;

    private final FareAvailabilityRepository fareAvailabilityRepository;

    @Override
    @Transactional
    public List<FareAvailabilityEntity> createFareAvailability(List<FareAvailbility> fareAvailability, FlightEntity flightEntity) {

        List<FareAvailabilityEntity> fareAvailabilityEntities = new ArrayList<>();

        for(FareAvailbility fare : fareAvailability) {
            FareClassEntity fareClassEntity = fareClassService.getFareClass(fare.getCodeFareClass());
            int availableSeatCount = seatReservationService.countAvailableSeat(flightEntity, fareClassEntity);
            FareAvailabilityEntity fareAvailabilityEntity = FareAvailabilityEntity.builder()
                    .availableSeats(availableSeatCount)
                    .basePrice(fare.getBasePrice())
                    .fareClassEntity(fareClassEntity)
                    .flightEntity(flightEntity)
                    .build();
            fareAvailabilityEntities.add(fareAvailabilityEntity);
        }


        return fareAvailabilityRepository.saveAll(fareAvailabilityEntities);
    }
}
