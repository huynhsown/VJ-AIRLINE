package com.vietjoke.vn.config.seeding.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.entity.fleet.AircraftEntity;
import com.vietjoke.vn.entity.fleet.AircraftModelEntity;
import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.repository.fleet.AircraftModelRepository;
import com.vietjoke.vn.repository.fleet.AircraftRepository;
import com.vietjoke.vn.repository.fleet.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AirlineSeeder implements CommandLineRunner {

    private final ObjectMapper objectMapper;

    private final AirlineRepository airlineRepository;
    private final AircraftRepository aircraftRepository;
    private final AircraftModelRepository aircraftModelRepository;

    public List<AirlineEntity> seedAirlines() {
        try{
            InputStream inputStream = new ClassPathResource("/data/airline_data.json").getInputStream();
            return List.of(objectMapper.readValue(inputStream, AirlineEntity[].class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) {

        if(airlineRepository.count() != 0) {
            return;
        }

        List<AirlineEntity> airlines = seedAirlines();

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
}
