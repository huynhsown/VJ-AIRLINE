package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.location.AirportDTO;
import com.vietjoke.vn.entity.location.AirportEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirportConverter {

    private final ModelMapper modelMapper;

    public AirportDTO toAirportDTO(AirportEntity airportEntity) {
        return modelMapper.map(airportEntity, AirportDTO.class);
    }

}
