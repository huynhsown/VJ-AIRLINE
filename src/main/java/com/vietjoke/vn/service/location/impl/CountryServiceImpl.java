package com.vietjoke.vn.service.location.impl;

import com.vietjoke.vn.converter.CountryConverter;
import com.vietjoke.vn.dto.location.CountryDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.location.CountryEntity;
import com.vietjoke.vn.repository.location.CountryRepository;
import com.vietjoke.vn.service.location.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CountryConverter countryConverter;

    @Override
    public CountryEntity getCountry(String code) {
        return countryRepository.findByCountryCode(code)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    @Override
    public ResponseDTO<List<CountryDTO>> getCountries() {
        List<CountryEntity> countryEntities = countryRepository.findAll();
        return ResponseDTO.success(countryEntities.stream()
                .map(countryConverter::toCountryDTO)
                .toList());
    }
}
