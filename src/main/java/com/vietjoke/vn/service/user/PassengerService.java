package com.vietjoke.vn.service.user;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.BookingPreviewDTO;
import com.vietjoke.vn.dto.response.user.PassengerInfoResponseDTO;
import com.vietjoke.vn.entity.user.PassengerEntity;

import java.util.Map;

public interface PassengerService {

    ResponseDTO<Map<String, String>> inputPassengerInfo(PassengersInfoParamDTO infoParamDTO);
    PassengerInfoResponseDTO getPassengerInfo(String sessionToken);
    PassengerEntity createPassenger(BookingPreviewDTO.PassengerBookingDetailDTO passengerBookingDetailDTO);
    PassengerEntity getOrCreatePassenger(BookingPreviewDTO.PassengerBookingDetailDTO passengerBookingDetailDTO);
}
