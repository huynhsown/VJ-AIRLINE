package com.vietjoke.vn.repository.fleet;

import com.vietjoke.vn.entity.fleet.AircraftModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AircraftModelRepository extends JpaRepository<AircraftModelEntity, Long> {

    boolean existsByModelCode(String modelCode);
    Optional<AircraftModelEntity> findByModelCode(String modelCode);

}
