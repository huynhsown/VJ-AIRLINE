package com.vietjoke.vn.config.seeding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.entity.user.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AirlineSeeder implements CommandLineRunner {
    private final ObjectMapper objectMapper;

    public List<AirlineEntity> seedAirlines() {
        try{
            InputStream inputStream = new ClassPathResource("/data/airlines.json").getInputStream();
            return List.of(objectMapper.readValue(inputStream, AirlineEntity[].class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
