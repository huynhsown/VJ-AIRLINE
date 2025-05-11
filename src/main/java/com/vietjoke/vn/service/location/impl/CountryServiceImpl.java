package com.vietjoke.vn.service.location.impl;

import com.vietjoke.vn.entity.location.CountryEntity;
import com.vietjoke.vn.repository.location.CountryRepository;
import com.vietjoke.vn.service.location.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public CountryEntity getCountry(String code) {
        return countryRepository.findByCountryCode(code)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }
}
