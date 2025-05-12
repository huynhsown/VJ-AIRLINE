package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.location.CountryDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountryConverter {

    private final ModelMapper modelMapper;

}
