package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddonConverter {

    private final ModelMapper modelMapper;

    public AddonDTO toAddonDTO(AddonEntity addonEntity) {
        return modelMapper.map(addonEntity, AddonDTO.class);
    }

}
