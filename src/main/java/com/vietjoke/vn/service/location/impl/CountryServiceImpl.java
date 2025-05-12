package com.vietjoke.vn.service.location.impl;

import com.vietjoke.vn.dto.location.CountryDTO;
import com.vietjoke.vn.entity.location.CountryEntity;
import com.vietjoke.vn.repository.location.CountryRepository;
import com.vietjoke.vn.service.location.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public CountryEntity getCountry(String code) {
        return countryRepository.findByCountryCode(code)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    @Override
    public List<CountryDTO> getCountries() {
        return countryRepository.findAll();
    }
}
