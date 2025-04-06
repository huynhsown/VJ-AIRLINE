package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.response.flight.FlightResponseDTO;
import com.vietjoke.vn.entity.flight.FlightEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightConverter {

    private final ModelMapper modelMapper;

    public FlightResponseDTO convertToResponseDTO(FlightEntity flightEntity) {
        FlightResponseDTO flightResponseDTO = modelMapper.map(flightEntity, FlightResponseDTO.class);
        flightResponseDTO.setFlightModelCode(flightEntity.getAircraftEntity().getAircraftModelEntity().getModelCode());
        return flightResponseDTO;
    }

}
