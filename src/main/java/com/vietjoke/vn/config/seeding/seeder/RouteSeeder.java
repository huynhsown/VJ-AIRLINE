//package com.vietjoke.vn.config.seeding.seeder;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.vietjoke.vn.config.seeding.jsonObject.Route;
//import com.vietjoke.vn.entity.fleet.AirlineEntity;
//import com.vietjoke.vn.entity.fleet.RouteEntity;
//import com.vietjoke.vn.entity.location.AirportEntity;
//import com.vietjoke.vn.repository.fleet.AirlineRepository;
//import com.vietjoke.vn.repository.fleet.RouteRepository;
//import com.vietjoke.vn.repository.location.AirportRepository;
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
//public class RouteSeeder implements CommandLineRunner {
//
//    private final ObjectMapper objectMapper;
//    private final AirportRepository airportRepository;
//    private final RouteRepository routeRepository;
//    private final AirlineRepository airlineRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try{
//            InputStream inputStream = new ClassPathResource("/data/routes.json").getInputStream();
//            List<Route> routeEntities = List.of(objectMapper.readValue(inputStream, Route[].class));
//
//            for(Route route : routeEntities){
//                Optional<AirportEntity> origin = airportRepository.findByAirportCode(route.getOriginAirportEntity());
//                Optional<AirportEntity> destination = airportRepository.findByAirportCode(route.getDestinationAirportEntity());
//                Optional<AirlineEntity> airlineEntity = airlineRepository.findByAirlineCode("VJ");
//                if(origin.isPresent()
//                        && destination.isPresent()
//                        && airlineEntity.isPresent()
//                && !routeRepository.existsByOriginAirportEntityAndDestinationAirportEntity(origin.get(), destination.get())){
//                    RouteEntity routeEntity = new RouteEntity();
//                    routeEntity.setOriginAirportEntity(origin.get());
//                    routeEntity.setDestinationAirportEntity(destination.get());
//                    routeEntity.setRouteCode(route.getOriginAirportEntity() + "-" + route.getDestinationAirportEntity());
//                    routeEntity.setActive(true);
//                    routeEntity.setDistance(route.getDistance());
//                    routeEntity.setEstimatedDuration(route.getEstimatedDuration());
//                    routeEntity.setAirlineEntity(airlineEntity.get());
//                    routeRepository.save(routeEntity);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
