package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.repository.pricing.FareClassRepository;
import com.vietjoke.vn.service.pricing.FareClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FareClassServiceImpl implements FareClassService {

    private final FareClassRepository fareClassRepository;

    @Override
    public FareClassEntity getFareClass(String code) {
        return fareClassRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("FareClass not found: " + code));
    }
}
