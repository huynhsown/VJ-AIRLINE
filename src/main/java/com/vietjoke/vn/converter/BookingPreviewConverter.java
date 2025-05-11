package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.pricing.AddonSelectionDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.dto.user.PassengerDTO;
import com.vietjoke.vn.entity.flight.FlightEntity;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.entity.pricing.FareAvailabilityEntity;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.util.enums.user.PassengerType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingPreviewConverter {

    private final ModelMapper modelMapper;

    public BookingPreviewDTO.AddonDetailDTO toAddonDetailDTO(AddonEntity addonEntity,
                                                             AddonSelectionDTO addonSelectionDTO) {
        return BookingPreviewDTO.AddonDetailDTO
                .builder()
                .addonId(addonEntity.getId())
                .addonName(addonEntity.getName())
                .price(addonEntity.getPrice())
                .quantity(addonSelectionDTO.getQuantity())
                .build();
    }

    public BookingPreviewDTO.PassengerFlightDetailDTO toPassengerFlightDetailDTO(
            FlightEntity flightEntity,
            FareAvailabilityEntity fareAvailEntity,
            List<BookingPreviewDTO.AddonDetailDTO> addonDetailDTOS) {

        BigDecimal totalPrice = addonDetailDTOS.stream()
                .map(addon -> addon.getPrice().multiply(BigDecimal.valueOf(addon.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BookingPreviewDTO.PassengerFlightDetailDTO
                .builder()
                .flightNumber(flightEntity.getFlightNumber())
                .ticketPrice(fareAvailEntity.getBasePrice())
                .fareClass(fareAvailEntity.getFareClassEntity().getName())
                .routeCode(flightEntity.getRouteEntity().getRouteCode())
                .addonPrice(totalPrice)
                .addons(addonDetailDTOS)
                .build();
    }

    public BookingPreviewDTO.PassengerBookingDetailDTO toPassengerBookingDetailDTO(
            PassengerDTO passengerDTO,
            List<BookingPreviewDTO.PassengerFlightDetailDTO> passengerFlightDetailDTOS) {

        return BookingPreviewDTO.PassengerBookingDetailDTO
                .builder()
                .passengerUuid(passengerDTO.getUuid())
                .firstName(passengerDTO.getFirstName())
                .lastName(passengerDTO.getLastName())
                .passengerType(passengerDTO.getPassengerType())
                .passengerFlightDetailDTOS(passengerFlightDetailDTOS)
                .accompanyingAdultFirstName(passengerDTO.getAccompanyingAdultFirstName())
                .accompanyingAdultLastName(passengerDTO.getAccompanyingAdultLastName())
                .dateOfBirth(passengerDTO.getDateOfBirth())
                .gender(passengerDTO.getGender())
                .countryCode(passengerDTO.getCountryCode())
                .idType(passengerDTO.getIdType())
                .idNumber(passengerDTO.getIdNumber())
                .phone(passengerDTO.getPhone())
                .email(passengerDTO.getEmail())
                .build();
    }

}
