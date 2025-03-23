package com.vietjoke.vn.config.seeding;

import com.vietjoke.vn.entity.fleet.AircraftModelEntity;
import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.entity.location.CountryEntity;
import com.vietjoke.vn.entity.user.RoleEntity;
import com.vietjoke.vn.repository.fleet.AircraftModelRepository;
import com.vietjoke.vn.repository.fleet.AirlineRepository;
import com.vietjoke.vn.repository.location.CountryRepository;
import com.vietjoke.vn.repository.user.RoleRepository;
import com.vietjoke.vn.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {


    private final AircraftModelRepository aircraftModelRepository;
    private final AirlineRepository airlineRepository;

    private final AircraftModelSeeder aircraftModelSeeder;
    private final AirlineSeeder airlineSeeder;

    @Override
    public void run(String... args) throws Exception {
        seedAirlines();
        seedAircraftModels();
    }

    private void seedAircraftModels() {
        if(aircraftModelRepository.count() == 0) {
            aircraftModelRepository.saveAll(aircraftModelSeeder.seedAircraftModels());
        }
    }

    private void seedAirlines() {
        if(airlineRepository.count() == 0) {
            airlineRepository.saveAll(airlineSeeder.seedAirlines());
        }
    }
}
