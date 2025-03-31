package com.vietjoke.vn.service.fleet.impl;

import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.repository.fleet.AirlineRepository;
import com.vietjoke.vn.service.fleet.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineEntity getAirlineEntity(String airlineCode) {
        return airlineRepository.findByAirlineCode(airlineCode)
                .orElseThrow(() -> new RuntimeException("Airline not found: " + airlineCode));
    }

}
