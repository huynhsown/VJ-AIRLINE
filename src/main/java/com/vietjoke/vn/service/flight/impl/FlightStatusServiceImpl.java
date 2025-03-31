package com.vietjoke.vn.service.flight.impl;

import com.vietjoke.vn.entity.flight.FlightStatusEntity;
import com.vietjoke.vn.repository.flight.FlightStatusRepository;
import com.vietjoke.vn.service.flight.FlightStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightStatusServiceImpl implements FlightStatusService {
    private final FlightStatusRepository flightStatusRepository;

    public FlightStatusEntity getFlightStatusEntity(String statusCode) {
        return flightStatusRepository.findByStatusCode(statusCode)
                .orElseThrow(() -> new RuntimeException("Flight status not found: " + statusCode));
    }
}
