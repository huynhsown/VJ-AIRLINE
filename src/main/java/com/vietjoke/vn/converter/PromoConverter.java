package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.pricing.PromoCodeDTO;
import com.vietjoke.vn.dto.pricing.PromoDTO;
import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PromoConverter {
    private final ModelMapper modelMapper;

    public PromoCodeEntity toPromoEntity(PromoCodeDTO promoCodeDTO) {
        return modelMapper.map(promoCodeDTO, PromoCodeEntity.class);
    }

    public PromoDTO toPromoDTO(PromoCodeEntity promoCodeEntity, boolean isAvailable) {
        if (!isAvailable || promoCodeEntity == null) {
            return PromoDTO.builder()
                    .isAvailable(false)
                    .build();
        }

        PromoDTO promoDTO = modelMapper.map(promoCodeEntity, PromoDTO.class);
        promoDTO.setIsAvailable(true);
        return promoDTO;
    }
}
