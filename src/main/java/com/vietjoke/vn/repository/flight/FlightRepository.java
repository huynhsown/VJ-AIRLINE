package com.vietjoke.vn.repository.flight;

import com.vietjoke.vn.entity.flight.FlightEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<FlightEntity, Long> {

    @Query("SELECT f FROM FlightEntity f " +
            "JOIN f.routeEntity r " +
            "WHERE r.routeCode = :routeCode " +
            "AND FUNCTION('DATE', f.scheduledDeparture) = :tripStartDate")
    List<FlightEntity> findFlightsBySearchParam(
            @Param("routeCode") String routeCode,
            @Param("tripStartDate") LocalDate tripStartDate);

    @Query("SELECT f FROM FlightEntity f " +
            "JOIN f.routeEntity r " +
            "WHERE r.routeCode = :routeCode")
    List<FlightEntity> test(
            @Param("routeCode") String routeCode);

}
