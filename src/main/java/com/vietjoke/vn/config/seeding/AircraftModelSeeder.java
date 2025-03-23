package com.vietjoke.vn.config.seeding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.entity.fleet.AircraftEntity;
import com.vietjoke.vn.entity.fleet.AircraftModelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Component
    @RequiredArgsConstructor
    public class AircraftModelSeeder {

        private final ObjectMapper objectMapper;

    public List<AircraftModelEntity> seedAircraftModels() {
        try{
            InputStream inputStream = new ClassPathResource("/data/aircraft_models.json").getInputStream();
            return List.of(objectMapper.readValue(inputStream, AircraftModelEntity[].class));
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
