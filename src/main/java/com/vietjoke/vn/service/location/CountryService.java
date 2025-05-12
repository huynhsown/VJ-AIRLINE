package com.vietjoke.vn.service.location;

import com.vietjoke.vn.dto.location.CountryDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.location.CountryEntity;

import java.util.List;

public interface CountryService {
    CountryEntity getCountry(String code);
    ResponseDTO<List<CountryDTO>> getCountries();
}
