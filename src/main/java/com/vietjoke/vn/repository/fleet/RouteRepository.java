package com.vietjoke.vn.repository.fleet;

import com.vietjoke.vn.entity.fleet.RouteEntity;
import com.vietjoke.vn.entity.location.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    boolean existsByOriginAirportEntityAndDestinationAirportEntity(AirportEntity originAirportEntity, AirportEntity destinationAirportEntity);

    Optional<RouteEntity> findByRouteCode(String routeCode);

}
