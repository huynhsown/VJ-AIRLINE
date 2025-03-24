package com.vietjoke.vn.repository.fleet;

import com.vietjoke.vn.entity.fleet.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<AircraftEntity, Long> {

    boolean existsByRegistrationNumber(String registrationNumber);

}
