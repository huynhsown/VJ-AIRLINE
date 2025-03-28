package com.vietjoke.vn.repository.fleet;

import com.vietjoke.vn.entity.fleet.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AircraftRepository extends JpaRepository<AircraftEntity, Long> {

    boolean existsByRegistrationNumber(String registrationNumber);
    Optional<AircraftEntity> findByRegistrationNumber(String registrationNumber);

}
