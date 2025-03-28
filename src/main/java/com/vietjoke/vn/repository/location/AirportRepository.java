package com.vietjoke.vn.repository.location;

import com.vietjoke.vn.entity.location.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<AirportEntity, Long> {

    Optional<AirportEntity> findByAirportCode(String airportCode);

}
