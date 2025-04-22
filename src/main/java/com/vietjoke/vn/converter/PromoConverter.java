package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.pricing.PromoCodeDTO;
import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromoConverter {
    private final ModelMapper modelMapper;

    public PromoCodeEntity toPromoEntity(PromoCodeDTO promoCodeDTO) {
        return modelMapper.map(promoCodeDTO, PromoCodeEntity.class);
    }
}
