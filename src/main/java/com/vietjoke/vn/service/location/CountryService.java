package com.vietjoke.vn.service.location;

import com.vietjoke.vn.dto.location.CountryDTO;
import com.vietjoke.vn.entity.location.CountryEntity;

import java.util.List;

public interface CountryService {
    CountryEntity getCountry(String code);
    List<CountryDTO> getCountries();
}
