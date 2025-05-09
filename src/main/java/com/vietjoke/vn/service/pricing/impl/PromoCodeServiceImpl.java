package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.entity.pricing.PromoCodeEntity;
import com.vietjoke.vn.repository.pricing.PromoRepository;
import com.vietjoke.vn.service.pricing.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoRepository promoRepository;

    @Override
    public PromoCodeEntity getPromoCode(String promoCode) {
        return promoRepository.findByCode(promoCode)
                .orElse(null);
    }
}
