package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.fleet.RouteDTO;
import com.vietjoke.vn.entity.fleet.RouteEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteConverter {

    private final ModelMapper modelMapper;

    public RouteDTO convertToDTO(RouteEntity routeEntity) {
        return modelMapper.map(routeEntity, RouteDTO.class);
    }

}
