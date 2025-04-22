package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.converter.AddonTypeConverter;
import com.vietjoke.vn.dto.pricing.AddonTypeDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.pricing.AddonTypeEntity;
import com.vietjoke.vn.repository.pricing.AddonTypeRepository;
import com.vietjoke.vn.service.pricing.AddonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddonTypeServiceImpl implements AddonTypeService {

    private final AddonTypeRepository addonTypeRepository;

    private final AddonTypeConverter addonTypeConverter;

    @Override
    public ResponseDTO<List<AddonTypeDTO>> getAllAddonTypes() {
        return ResponseDTO.success(addonTypeRepository.findAll().stream()
                .map(addonTypeConverter::toAddonTypeDTO)
                .toList());
    }
}
