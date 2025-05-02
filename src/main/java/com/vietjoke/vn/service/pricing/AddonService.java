package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.dto.request.pricing.AddonBookingRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.pricing.FlightServiceResponseDTO;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.util.enums.pricing.AddonStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AddonService {
    AddonEntity getAddonById(long id);

    ResponseDTO<FlightServiceResponseDTO> getAddonsByType(
            String addonCode,
            String sortBy,
            String sortOrder,
            int pageNumber,
            int pageSize,
            AddonStatus addonStatus,
            String sessionToken);

    ResponseDTO<FlightServiceResponseDTO> getAddonsForFlight(
            String addonCode,
            String sortBy,
            String sortOrder,
            int pageNumber,
            int pageSize,
            AddonStatus addonStatus,
            String sessionToken,
            String flightNumber
    );

    ResponseDTO<?> bookAddons(AddonBookingRequestDTO requestDTO);
    ResponseDTO<?> getPassengerAddons(String sessionToken, String flightNumber, String passengerUuid);
}
