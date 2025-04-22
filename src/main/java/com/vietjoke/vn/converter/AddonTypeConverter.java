package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.pricing.AddonTypeDTO;
import com.vietjoke.vn.entity.pricing.AddonTypeEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddonTypeConverter {

    private final ModelMapper modelMapper;

    public AddonTypeDTO toAddonTypeDTO(AddonTypeEntity addonTypeEntity) {
        return modelMapper.map(addonTypeEntity, AddonTypeDTO.class);
    }

}
