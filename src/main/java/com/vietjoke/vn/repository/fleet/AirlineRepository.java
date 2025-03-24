package com.vietjoke.vn.repository.fleet;

import com.vietjoke.vn.entity.fleet.AirlineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<AirlineEntity, Long> {
    boolean existsByAirlineCode(String code);
    Optional<AirlineEntity> findByAirlineCode(String code);
}
