package com.vietjoke.vn.converter;

import com.vietjoke.vn.config.seeding.jsonObject.FareClass;
import com.vietjoke.vn.dto.response.FareClassDTO;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FareClassConverter {

    private final ModelMapper modelMapper;

    public FareClassDTO convertToDTO(FareAvailabilityEntity fareAvailabilityEntity) {
        return modelMapper.map(fareAvailabilityEntity, FareClassDTO.class);
    }

}
