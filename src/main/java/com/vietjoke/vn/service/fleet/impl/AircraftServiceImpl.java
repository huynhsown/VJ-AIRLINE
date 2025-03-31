package com.vietjoke.vn.service.fleet.impl;

import com.vietjoke.vn.entity.fleet.AircraftEntity;
import com.vietjoke.vn.repository.fleet.AircraftRepository;
import com.vietjoke.vn.service.fleet.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {
    private final AircraftRepository aircraftRepository;

    public AircraftEntity getAircraftEntity(String registerNumber) {
        return aircraftRepository.findByRegistrationNumber(registerNumber)
                .orElseThrow(() -> new RuntimeException("Aircraft not found: " + registerNumber));
    }
}
