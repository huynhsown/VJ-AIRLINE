package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.location.CountryDTO;
import com.vietjoke.vn.entity.location.CountryEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountryConverter {

    private final ModelMapper modelMapper;

    public CountryDTO toCountryDTO(CountryEntity countryEntity) {
        return modelMapper.map(countryEntity, CountryDTO.class);
    }

}
