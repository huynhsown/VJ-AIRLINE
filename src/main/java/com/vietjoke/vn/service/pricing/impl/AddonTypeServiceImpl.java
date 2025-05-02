package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.converter.AddonTypeConverter;
import com.vietjoke.vn.dto.pricing.AddonTypeDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.repository.pricing.AddonTypeRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
import com.vietjoke.vn.service.pricing.AddonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddonTypeServiceImpl implements AddonTypeService {

    private final AddonTypeRepository addonTypeRepository;

    private final AddonTypeConverter addonTypeConverter;

    private final BookingSessionService bookingSessionService;

    @Override
    public ResponseDTO<List<AddonTypeDTO>> getAllAddonTypes(String sessionToken) {
        if (sessionToken == null) {
            return getAddonTypes();
        }

        BookingSession session = bookingSessionService.getSession(sessionToken);
        BookingSessionHelper.validateServiceBookingSteps(session);

        return getAddonTypes();
    }

    private ResponseDTO<List<AddonTypeDTO>> getAddonTypes() {
        List<AddonTypeDTO> addonTypes = addonTypeRepository.findAll().stream()
                .map(addonTypeConverter::toAddonTypeDTO)
                .collect(Collectors.toList());
        return ResponseDTO.success(addonTypes);
    }
}
