//package com.vietjoke.vn.config.seeding.seeder;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.vietjoke.vn.config.seeding.jsonObject.FareClass;
//import com.vietjoke.vn.config.seeding.jsonObject.Flight;
//import com.vietjoke.vn.config.seeding.jsonObject.Route;
//import com.vietjoke.vn.entity.fleet.AircraftEntity;
//import com.vietjoke.vn.entity.fleet.AirlineEntity;
//import com.vietjoke.vn.entity.fleet.RouteEntity;
//import com.vietjoke.vn.entity.flight.FlightEntity;
//import com.vietjoke.vn.entity.flight.FlightStatusEntity;
//import com.vietjoke.vn.entity.pricing.FareClassEntity;
//import com.vietjoke.vn.repository.fleet.AircraftRepository;
//import com.vietjoke.vn.repository.fleet.AirlineRepository;
//import com.vietjoke.vn.repository.fleet.RouteRepository;
//import com.vietjoke.vn.repository.flight.FlightRepository;
//import com.vietjoke.vn.repository.flight.FlightStatusRepository;
//import com.vietjoke.vn.repository.pricing.FareClassRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.io.InputStream;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class FlightSeeder implements CommandLineRunner {
//
//    private final FareClassRepository fareClassRepository;
//    private final RouteRepository routeRepository;
//    private final ObjectMapper objectMapper;
//    private final AirlineRepository airlineRepository;
//    private final FlightStatusRepository flightStatusRepository;
//    private final FlightRepository flightRepository;
//    private final AircraftRepository aircraftRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if(flightRepository.count() != 0) {
//            return;
//        }
//        InputStream inputStream = new ClassPathResource("/data/flights.json").getInputStream();
//        List<Flight> flights = List.of(objectMapper.readValue(inputStream, Flight[].class));
//        for(Flight flight : flights) {
//            FlightEntity flightEntity = new FlightEntity();
//            AirlineEntity airlineEntity = airlineRepository.findByAirlineCode(flight.getAirlineCode())
//                    .orElseThrow(() -> new RuntimeException("Airline not found: " + flight.getAirlineCode()));
//            RouteEntity routeEntity = routeRepository.findByRouteCode(flight.getRouteCode())
//                    .orElseThrow(() -> new RuntimeException("Route not found: " + flight.getRouteCode()));
//
//            flightEntity.setScheduledDeparture(flight.getScheduledDeparture());
//            flightEntity.setScheduledArrival(flight.getScheduledArrival());
//            flightEntity.setGate(flight.getGate());
//            flightEntity.setTerminal(flight.getTerminal());
//            flightEntity.setAirlineEntity(airlineEntity);
//            flightEntity.setRouteEntity(routeEntity);
//
//            if (flight.getStatusCode() != null) {
//                FlightStatusEntity statusEntity = flightStatusRepository.findByStatusCode(flight.getStatusCode())
//                        .orElseThrow(() -> new RuntimeException("Flight status not found: " + flight.getStatusCode()));
//                flightEntity.setFlightStatusEntity(statusEntity);
//            }
//
//            if (flight.getRegisterNumber() != null) {
//                AircraftEntity aircraftEntity = aircraftRepository.findByRegistrationNumber(flight.getRegisterNumber())
//                        .orElseThrow(() -> new RuntimeException("Aircraft not found: " + flight.getRegisterNumber()));
//                flightEntity.setAircraftEntity(aircraftEntity);
//            }
//
//            flightRepository.save(flightEntity);
//        }
//    }
//}
