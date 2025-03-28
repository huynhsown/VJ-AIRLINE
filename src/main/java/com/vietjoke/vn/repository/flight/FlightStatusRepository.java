package com.vietjoke.vn.repository.flight;

import com.vietjoke.vn.entity.flight.FlightStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightStatusRepository extends JpaRepository<FlightStatusEntity, Long> {

    Optional<FlightStatusEntity> findByStatusCode(String statusCode);

}
