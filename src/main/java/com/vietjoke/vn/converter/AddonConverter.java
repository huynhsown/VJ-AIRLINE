package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AddonConverter {

    private final ModelMapper modelMapper;

    public AddonDTO toAddonDTO(AddonEntity addonEntity) {
        return modelMapper.map(addonEntity, AddonDTO.class);
    }

    public AddonDTO toAddonDTO(AddonEntity addonEntity, boolean isFree) {
        AddonDTO addonDTO = modelMapper.map(addonEntity, AddonDTO.class);
        if (isFree) addonDTO.setPrice(BigDecimal.ZERO);
        return addonDTO;
    }

}
