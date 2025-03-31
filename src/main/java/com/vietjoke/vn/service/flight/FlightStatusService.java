package com.vietjoke.vn.service.flight;

import com.vietjoke.vn.entity.flight.FlightStatusEntity;

public interface FlightStatusService {
    FlightStatusEntity getFlightStatusEntity(String statusCode);
}
