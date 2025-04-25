package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.converter.AddonConverter;
import com.vietjoke.vn.converter.FlightConverter;
import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.FlightResponseDTO;
import com.vietjoke.vn.dto.response.pricing.FlightServiceResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.exception.booking.MissingBookingStepException;
import com.vietjoke.vn.exception.user.PermissionDenyException;
import com.vietjoke.vn.repository.pricing.AddonRepository;
import com.vietjoke.vn.repository.pricing.FareClassAddonRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.service.pricing.FareClassService;
import com.vietjoke.vn.util.enums.pricing.AddonStatus;
import com.vietjoke.vn.util.enums.pricing.AddonType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddonServiceImpl implements AddonService {

    private final AddonRepository addonRepository;
    private final FareClassAddonRepository fareClassAddonRepository;

    private final AddonConverter addonConverter;
    private final FlightConverter flightConverter;

    private final BookingSessionService bookingSessionService;
    private final FlightService flightService;
    private final FareClassService fareClassService;


    public boolean isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    @Override
    public ResponseDTO<FlightServiceResponseDTO> getAddonsByType(String addonCode, String sortBy, String sortOrder,
                                                       int pageNumber, int pageSize, AddonStatus addonStatus,
                                                       String sessionToken) {
        if(addonStatus != AddonStatus.ACTIVE && !isUserAdmin()) {
            throw new PermissionDenyException("FORBIDDEN");
        }

        BookingSession session = bookingSessionService.getSession(sessionToken);
        SelectFlightRequestDTO selectFlightRequestDTO = session.getSelectedFlight();
        if(selectFlightRequestDTO == null) {
            throw new MissingBookingStepException("Flight selection step is missing");
        }
        PassengersInfoParamDTO paramDTO = session.getPassengersInfoParamDTO();
        if(paramDTO == null) {
            throw new MissingBookingStepException("Passengers info step is missing");
        }

        List<SelectFlightDTO> selectFlightDTO = selectFlightRequestDTO.getFlights();

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<AddonEntity> pageAddons = switch (addonStatus) {
            case ACTIVE -> addonRepository.findByAddonTypeEntity_AddonCodeAndIsActive(AddonType.valueOf(addonCode), true, pageable);
            case INACTIVE -> addonRepository.findByAddonTypeEntity_AddonCodeAndIsActive(AddonType.valueOf(addonCode), false, pageable);
            default -> addonRepository.findByAddonTypeEntity_AddonCode(AddonType.valueOf(addonCode), pageable);
        };

        List<FlightResponseDTO> flightResponseDTOs = selectFlightDTO.stream()
                .map(selectFlight -> {
                    FlightEntity flightEntity = flightService.getFlightByFlightNumber(selectFlight.getFlightNumber());
                    FareClassEntity fareClassEntity = fareClassService.getFareClass(selectFlight.getFareCode());
                    FlightResponseDTO flightResponseDTO = flightConverter.convertToResponseDTO(flightEntity);

                    List<AddonDTO> addonDTOList = pageAddons.getContent().stream()
                            .map(addonEntity -> {
                                boolean isInFareClass = fareClassAddonRepository.existsByAddonEntityAndFareClassEntity(
                                        addonEntity, fareClassEntity);
                                return addonConverter.toAddonDTO(addonEntity, isInFareClass);
                            })
                            .toList();

                    Page<AddonDTO> addonDTOPage = new PageImpl<>(addonDTOList, pageable, pageAddons.getTotalElements());
                    flightResponseDTO.setAddonDTOs(addonDTOPage);
                    return flightResponseDTO;
                })
                .toList();

        FlightServiceResponseDTO flightSeat = FlightServiceResponseDTO.builder()
                .sessionToken(session.getSessionId())
                .flight(flightResponseDTOs)
                .build();
        return ResponseDTO.success(flightSeat);
    }
}
