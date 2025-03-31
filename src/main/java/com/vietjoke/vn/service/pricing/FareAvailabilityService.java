package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.config.seeding.jsonObject.FareAvailbility;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;

import java.util.List;

public interface FareAvailabilityService {

    List<FareAvailabilityEntity> createFareAvailability(List<FareAvailbility> fareAvailability, FlightEntity flightEntity);

}
