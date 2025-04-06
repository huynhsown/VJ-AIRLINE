package com.vietjoke.vn.service.user;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.user.PassengerInfoResponseDTO;

import java.util.Map;

public interface PassengerService {

    ResponseDTO<Map<String, String>> inputPassengerInfo(PassengersInfoParamDTO infoParamDTO);
    PassengerInfoResponseDTO getPassengerInfo(SessionTokenRequestDTO sessionToken);

}
