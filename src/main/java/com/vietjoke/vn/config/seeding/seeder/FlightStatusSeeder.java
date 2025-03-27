package com.vietjoke.vn.config.seeding.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.entity.flight.FlightStatusEntity;
import com.vietjoke.vn.repository.flight.FlightStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightStatusSeeder implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final FlightStatusRepository flightStatusRepository;

    @Override
    public void run(String... args) throws Exception {
        try{
            InputStream inputStream = new ClassPathResource("/data/flight_status.json").getInputStream();
            List<FlightStatusEntity> flightStatusEntities = List.of(objectMapper.readValue(inputStream, FlightStatusEntity[].class));
            for(FlightStatusEntity flightStatusEntity : flightStatusEntities){
                if(flightStatusRepository.findByStatusCode(flightStatusEntity.getStatusCode()).isEmpty()){
                    flightStatusRepository.save(flightStatusEntity);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
