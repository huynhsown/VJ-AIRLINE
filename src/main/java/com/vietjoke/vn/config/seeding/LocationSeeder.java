package com.vietjoke.vn.config.seeding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietjoke.vn.entity.location.AirportEntity;
import com.vietjoke.vn.entity.location.CountryEntity;
import com.vietjoke.vn.entity.location.ProvinceEntity;
import com.vietjoke.vn.repository.location.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationSeeder implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {
        if (countryRepository.count() > 0) {
            return;
        }

        try {
            CountryEntity country = loadCountryData();
            prepareRelationships(country);
            countryRepository.save(country);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
}