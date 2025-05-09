package com.vietjoke.vn.service.helper;

import com.vietjoke.vn.converter.PromoConverter;
import com.vietjoke.vn.dto.pricing.PromoDTO;
import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import com.vietjoke.vn.service.pricing.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PromoValidator {
    private final PromoCodeService promoCodeService;
    private final PromoConverter promoConverter;

    public PromoDTO validateAndConvertPromo(String couponCode, int passengerCount, BigDecimal totalPrice) {
        PromoCodeEntity promoEntity = promoCodeService.getPromoCode(couponCode);
        boolean isAvailable = isPromoAvailable(promoEntity, passengerCount, totalPrice);
        return promoConverter.toPromoDTO(promoEntity, isAvailable);
    }

    private boolean isPromoAvailable(PromoCodeEntity promo, int passengerCount, BigDecimal totalPrice) {
        return promo != null
                && promo.getIsActive()
                && promo.getMinPassenger() <= passengerCount
                && promo.getMinBookingAmount().compareTo(totalPrice) <= 0;
    }
}
