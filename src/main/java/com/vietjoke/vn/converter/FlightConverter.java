package com.vietjoke.vn.converter;

import com.vietjoke.vn.config.seeding.jsonObject.Flight;
import com.vietjoke.vn.dto.response.FlightResponseDTO;
import com.vietjoke.vn.entity.flight.FlightEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightConverter {

    private final ModelMapper modelMapper;

    public FlightResponseDTO convertToResponseDTO(FlightEntity flightEntity) {
        return modelMapper.map(flightEntity, FlightResponseDTO.class);
    }

}
