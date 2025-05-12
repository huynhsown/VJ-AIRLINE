package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.config.seeding.jsonObject.FareAvailbility;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;

import java.util.List;
import java.util.Optional;

public interface FareAvailabilityService {

    List<FareAvailabilityEntity> createFareAvailability(List<FareAvailbility> fareAvailability, FlightEntity flightEntity);
    FareAvailabilityEntity getFareAvailability(FlightEntity flightEntity, FareClassEntity fareClassEntity);
    FareAvailabilityEntity getFareAvailability(String flightNumber, String fareClasCode);
    FareAvailabilityEntity decreaseAvailableSeat(String flightNumber, String fareClasCode);
}
