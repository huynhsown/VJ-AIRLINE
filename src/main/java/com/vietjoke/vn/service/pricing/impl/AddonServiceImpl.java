package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.converter.AddonConverter;
import com.vietjoke.vn.converter.FlightConverter;
import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.request.pricing.AddonBookingRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.FlightResponseDTO;
import com.vietjoke.vn.dto.response.pricing.FlightServiceResponseDTO;
import com.vietjoke.vn.dto.response.pricing.PassengerAddonsResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.exception.pricing.InvalidAddonQuantityException;
import com.vietjoke.vn.exception.user.PermissionDenyException;
import com.vietjoke.vn.repository.pricing.AddonRepository;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddonServiceImpl implements AddonService {

    private final AddonRepository addonRepository;

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
    public AddonEntity getAddonById(long id) {
        return addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon " + id + " not found"));
    }

    @Override
    public ResponseDTO<FlightServiceResponseDTO> getAddonsForFlight(
            String addonCode,
            String sortBy,
            String sortOrder,
            int pageNumber,
            int pageSize,
            AddonStatus addonStatus,
            String sessionToken,
            String flightNumber) {
        if(addonStatus != AddonStatus.ACTIVE && !isUserAdmin()) {
            throw new PermissionDenyException("FORBIDDEN");
        }

        BookingSession session = bookingSessionService.getSession(sessionToken);
        BookingSessionHelper.validateServiceBookingSteps(session, flightNumber);
        SelectFlightDTO selectFlightDTO = BookingSessionHelper.requireFlight(session, flightNumber);
        FlightEntity flightEntity = flightService.getFlightByFlightNumber(flightNumber);
        FareClassEntity fareClassEntity = fareClassService.getFareClass(selectFlightDTO.getFareCode());
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<AddonEntity> pageAddons;

        if(fareClassEntity.getMealIncluded()){
            pageAddons = addonRepository.findByAddonTypeEntity_AddonCodeAndIsActive(AddonType.valueOf(addonCode), true, pageable);
        }
        else{
            pageAddons = addonRepository.findByAddonTypeEntity_AddonCodeAndIsActiveAndIsFree(AddonType.valueOf(addonCode), true, false, pageable);
        }

        FlightResponseDTO flightResponseDTO = flightConverter.convertToResponseDTO(flightEntity);
        List<AddonDTO> addonDTOList = pageAddons.getContent().stream()
                .map(addonConverter::toAddonDTO)
                .toList();

        Page<AddonDTO> addonDTOPage = new PageImpl<>(addonDTOList, pageable, pageAddons.getTotalElements());
        flightResponseDTO.setAddonDTOs(addonDTOPage);
        return ResponseDTO.success(FlightServiceResponseDTO.builder()
                .sessionToken(sessionToken)
                .flight(List.of(flightResponseDTO))
                .build());
    }

    @Override
    public ResponseDTO<?> bookAddons(AddonBookingRequestDTO requestDTO) {
        BookingSession session = bookingSessionService.getSession(requestDTO.getSessionToken());
        BookingSessionHelper.validateServiceBookingSteps(session,
                requestDTO.getFlightNumber(),
                requestDTO.getPassengerUuid());

        String passengerUuid = requestDTO.getPassengerUuid();
        String flightNumber = requestDTO.getFlightNumber();

        if (session.getPassengerAddons() == null) {
            session.setPassengerAddons(new HashMap<>());
        }

        Map<String, List<AddonSelectionDTO>> flightAddons = session.getPassengerAddons()
                .computeIfAbsent(passengerUuid, k -> new HashMap<>());

        List<AddonSelectionDTO> currentAddons = flightAddons
                .computeIfAbsent(flightNumber, k -> new ArrayList<>());

        Map<Long, AddonSelectionDTO> addonMap = currentAddons.stream()
                .collect(Collectors.toMap(AddonSelectionDTO::getAddonId, a -> a));

        List<AddonSelectionDTO> newAddons = requestDTO.getAddons();

        for (AddonSelectionDTO newAddon : newAddons) {
            AddonEntity addonEntity = getAddonById(newAddon.getAddonId());
            if (newAddon.getQuantity() > addonEntity.getMaxQuantity()) {
                throw new InvalidAddonQuantityException(String.format("Addon '%s' vượt quá số lượng tối đa (%d)",
                        addonEntity.getName(), addonEntity.getMaxQuantity()));
            }
            if (addonMap.containsKey(newAddon.getAddonId())) {
                addonMap.get(newAddon.getAddonId()).setQuantity(newAddon.getQuantity());
            } else {
                AddonSelectionDTO addonToAdd = new AddonSelectionDTO(newAddon.getAddonId(), newAddon.getQuantity());
                addonMap.put(newAddon.getAddonId(), addonToAdd);
                currentAddons.add(addonToAdd);
            }
        }

        Set<Long> newAddonIds = newAddons.stream()
                .map(AddonSelectionDTO::getAddonId)
                .collect(Collectors.toSet());

        currentAddons.removeIf(addon -> !newAddonIds.contains(addon.getAddonId()));

        session = bookingSessionService.updateSelectedService(session.getSessionId(), session.getPassengerAddons());
        
        BookingSession verifySession = bookingSessionService.getSession(requestDTO.getSessionToken());
        System.out.println("Verify session: " + verifySession.getPassengerAddons());
        
        return ResponseDTO.success(Map.of("sessionToken", session.getSessionId(),
                "currentStep", session.getCurrentStep(),
                "nextStep", session.getNextStep()));
    }

    @Override
    public ResponseDTO<?> getPassengerAddons(String sessionToken, String flightNumber, String passengerUuid) {
        BookingSession session = bookingSessionService.getSession(sessionToken);
        BookingSessionHelper.validateServiceBookingSteps(session,
                flightNumber,
                passengerUuid);

        Map<String, Map<String, List<AddonSelectionDTO>>> passengerAddons = session.getPassengerAddons();
        if (passengerAddons == null) {
            System.out.println("passengerAddons is null");
            return ResponseDTO.success(PassengerAddonsResponseDTO.builder()
                    .passengerUuid(passengerUuid)
                    .flightNumber(flightNumber)
                    .addons(new ArrayList<>())
                    .build());
        }
        
        Map<String, List<AddonSelectionDTO>> flightAddons = passengerAddons.get(passengerUuid);
        if (flightAddons == null) {
            System.out.println("flightAddons is null for passenger: " + passengerUuid);
            return ResponseDTO.success(PassengerAddonsResponseDTO.builder()
                    .passengerUuid(passengerUuid)
                    .flightNumber(flightNumber)
                    .addons(new ArrayList<>())
                    .build());
        }
        
        List<AddonSelectionDTO> addons = flightAddons.get(flightNumber);
        if (addons == null) {
            System.out.println("addons is null for flight: " + flightNumber);
            addons = new ArrayList<>();
        }

        PassengerAddonsResponseDTO responseDTO = PassengerAddonsResponseDTO.builder()
                .passengerUuid(passengerUuid)
                .flightNumber(flightNumber)
                .addons(addons)
                .build();
        return ResponseDTO.success(responseDTO);
    }
}
