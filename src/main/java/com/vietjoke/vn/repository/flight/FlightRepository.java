package com.vietjoke.vn.repository.flight;

import com.vietjoke.vn.entity.flight.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
}
