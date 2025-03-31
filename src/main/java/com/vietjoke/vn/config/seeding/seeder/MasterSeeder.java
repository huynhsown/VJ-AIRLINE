package com.vietjoke.vn.config.seeding.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.config.seeding.jsonObject.FareClass;
import com.vietjoke.vn.config.seeding.jsonObject.Flight;
import com.vietjoke.vn.config.seeding.jsonObject.Route;
import com.vietjoke.vn.entity.fleet.AircraftEntity;
import com.vietjoke.vn.entity.fleet.AircraftModelEntity;
import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.entity.fleet.RouteEntity;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.flight.FlightStatusEntity;
import com.vietjoke.vn.entity.location.AirportEntity;
import com.vietjoke.vn.entity.location.CountryEntity;
import com.vietjoke.vn.entity.location.ProvinceEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.entity.user.RoleEntity;
import com.vietjoke.vn.repository.fleet.AircraftModelRepository;
import com.vietjoke.vn.repository.fleet.AircraftRepository;
import com.vietjoke.vn.repository.fleet.AirlineRepository;
import com.vietjoke.vn.repository.fleet.RouteRepository;
import com.vietjoke.vn.repository.flight.FlightRepository;
import com.vietjoke.vn.repository.flight.FlightStatusRepository;
import com.vietjoke.vn.repository.location.AirportRepository;
import com.vietjoke.vn.repository.location.CountryRepository;
import com.vietjoke.vn.repository.pricing.FareClassRepository;
import com.vietjoke.vn.repository.user.RoleRepository;
import com.vietjoke.vn.service.flight.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MasterSeeder implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final CountryRepository countryRepository;
    private final AirlineRepository airlineRepository;
    private final AircraftRepository aircraftRepository;
    private final AircraftModelRepository aircraftModelRepository;
    private final FareClassRepository fareClassRepository;
    private final FlightStatusRepository flightStatusRepository;
    private final FlightRepository flightRepository;
    private final RouteRepository routeRepository;
    private final AirportRepository airportRepository;
    private final RoleRepository roleRepository;

    private final FlightService flightService;

    @Override
    public void run(String... args) throws Exception {
        seedLocations();
        seedAirlines();
        seedFareClasses();
        seedFlightStatuses();
        seedRoutes();
        seedFlights();
        seedRoles();
    }

    // Location Seeder
    private void seedLocations() {
        if (countryRepository.count() > 0) {
            return;
        }
        try {
            CountryEntity country = loadCountryData();
            prepareRelationships(country);
            countryRepository.save(country);
        } catch (Exception e) {
            throw new RuntimeException("Error seeding locations", e);
        }
    }

    private CountryEntity loadCountryData() throws Exception {
        InputStream inputStream = new ClassPathResource("/data/vietnam_airports.json").getInputStream();
        return objectMapper.readValue(inputStream, CountryEntity.class);
    }

    private void prepareRelationships(CountryEntity country) {
        for (ProvinceEntity provinceEntity : country.getProvinceEntities()) {
            provinceEntity.setCountryEntity(country);
            for (AirportEntity airportEntity : provinceEntity.getAirportEntities()) {
                airportEntity.setProvinceEntity(provinceEntity);
            }
        }
    }

    // Airline Seeder
    private void seedAirlines() {
        if (airlineRepository.count() != 0) {
            return;
        }
        List<AirlineEntity> airlines = loadAirlineData();
        for (AirlineEntity airline : airlines) {
            for (AircraftEntity aircraftEntity : airline.getAircraftEntities()) {
                AircraftModelEntity aircraftModelEntity = aircraftEntity.getAircraftModelEntity();
                Optional<AircraftModelEntity> existingModel = aircraftModelRepository.findByModelCode(aircraftModelEntity.getModelCode());
                if (existingModel.isPresent()) {
                    aircraftModelEntity = existingModel.get();
                } else {
                    aircraftModelEntity = aircraftModelRepository.save(aircraftModelEntity);
                }
                aircraftEntity.setAircraftModelEntity(aircraftModelEntity);
                aircraftEntity.setAirlineEntity(airline);
            }
            Optional<AirlineEntity> existingAirline = airlineRepository.findByAirlineCode(airline.getAirlineCode());
            if (existingAirline.isPresent()) {
                airline = existingAirline.get();
            }
            airlineRepository.save(airline);
        }
    }

    private List<AirlineEntity> loadAirlineData() {
        try {
            InputStream inputStream = new ClassPathResource("/data/airline_data.json").getInputStream();
            return List.of(objectMapper.readValue(inputStream, AirlineEntity[].class));
        } catch (Exception e) {
            throw new RuntimeException("Error loading airline data", e);
        }
    }

    // FareClass Seeder
    private void seedFareClasses() throws Exception {
        InputStream inputStream = new ClassPathResource("/data/fareclasses.json").getInputStream();
        List<FareClass> fareClasses = List.of(objectMapper.readValue(inputStream, FareClass[].class));
        for (FareClass fareClass : fareClasses) {
            Optional<AirlineEntity> airlineEntity = airlineRepository.findByAirlineCode(fareClass.getAirlineCode());
            if (fareClassRepository.existsByCode(fareClass.getCode()) || airlineEntity.isEmpty()) {
                continue;
            }
            FareClassEntity fareClassEntity = getFareClassEntity(fareClass, airlineEntity);
            fareClassRepository.save(fareClassEntity);
        }
    }

    private static FareClassEntity getFareClassEntity(FareClass fareClass, Optional<AirlineEntity> airlineEntity) {
        FareClassEntity fareClassEntity = new FareClassEntity();
        fareClassEntity.setCode(fareClass.getCode());
        fareClassEntity.setName(fareClass.getName());
        fareClassEntity.setDescription(fareClass.getDescription());
        fareClassEntity.setIsActive(fareClass.isActive());
        fareClassEntity.setBaggageAllowance(fareClass.getBaggageAllowance());
        fareClassEntity.setSeatSelection(fareClass.isSeatSelection());
        fareClassEntity.setMealIncluded(fareClass.isMealIncluded());
        fareClassEntity.setChangeAllowed(fareClass.isChangeAllowed());
        fareClassEntity.setRefundAllowed(fareClass.isRefundAllowed());
        fareClassEntity.setChangeFee(fareClass.getChangeFee());
        fareClassEntity.setRefundFee(fareClass.getRefundFee());
        fareClassEntity.setAirlineEntity(airlineEntity.get());
        return fareClassEntity;
    }

    // FlightStatus Seeder
    private void seedFlightStatuses() throws Exception {
        InputStream inputStream = new ClassPathResource("/data/flight_status.json").getInputStream();
        List<FlightStatusEntity> flightStatusEntities = List.of(objectMapper.readValue(inputStream, FlightStatusEntity[].class));
        for (FlightStatusEntity flightStatusEntity : flightStatusEntities) {
            if (flightStatusRepository.findByStatusCode(flightStatusEntity.getStatusCode()).isEmpty()) {
                flightStatusRepository.save(flightStatusEntity);
            }
        }
    }

    // Route Seeder
    private void seedRoutes() throws Exception {
        InputStream inputStream = new ClassPathResource("/data/routes.json").getInputStream();
        List<Route> routeEntities = List.of(objectMapper.readValue(inputStream, Route[].class));
        for (Route route : routeEntities) {
            Optional<AirportEntity> origin = airportRepository.findByAirportCode(route.getOriginAirportEntity());
            Optional<AirportEntity> destination = airportRepository.findByAirportCode(route.getDestinationAirportEntity());
            Optional<AirlineEntity> airlineEntity = airlineRepository.findByAirlineCode("VJ");
            if (origin.isPresent() && destination.isPresent() && airlineEntity.isPresent()
                    && !routeRepository.existsByOriginAirportEntityAndDestinationAirportEntity(origin.get(), destination.get())) {
                RouteEntity routeEntity = new RouteEntity();
                routeEntity.setOriginAirportEntity(origin.get());
                routeEntity.setDestinationAirportEntity(destination.get());
                routeEntity.setRouteCode(route.getOriginAirportEntity() + "-" + route.getDestinationAirportEntity());
                routeEntity.setActive(true);
                routeEntity.setDistance(route.getDistance());
                routeEntity.setEstimatedDuration(route.getEstimatedDuration());
                routeEntity.setAirlineEntity(airlineEntity.get());
                routeRepository.save(routeEntity);
            }
        }
    }

    // Flight Seeder
    private void seedFlights() throws Exception {
        if (flightRepository.count() != 0) {
            return;
        }
        InputStream inputStream = new ClassPathResource("/data/flights.json").getInputStream();
        List<Flight> flights = List.of(objectMapper.readValue(inputStream, Flight[].class));
        for (Flight flight : flights) {
            flightService.createOrUpdateFlight(flight);
        }
    }

    // Role Seeder
    private void seedRoles() {
        List<RoleEntity> roles = loadRoleData();
        for (RoleEntity role : roles) {
            if (!roleRepository.existsByRoleCode(role.getRoleCode())) {
                roleRepository.save(role);
            }
        }
    }

    private List<RoleEntity> loadRoleData() {
        try {
            InputStream inputStream = new ClassPathResource("/data/roles.json").getInputStream();
            return List.of(objectMapper.readValue(inputStream, RoleEntity[].class));
        } catch (Exception e) {
            throw new RuntimeException("Error loading role data", e);
        }
    }
}