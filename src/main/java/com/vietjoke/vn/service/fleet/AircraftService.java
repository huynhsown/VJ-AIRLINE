package com.vietjoke.vn.service.fleet;

import com.vietjoke.vn.entity.fleet.AircraftEntity;

public interface AircraftService {
    AircraftEntity getAircraftEntity(String registerNumber);
}
