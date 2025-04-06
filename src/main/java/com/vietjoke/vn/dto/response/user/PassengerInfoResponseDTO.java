package com.vietjoke.vn.dto.response.user;

import com.vietjoke.vn.dto.user.PassengerDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PassengerInfoResponseDTO {
    private String sessionToken;
    List<SimplifiedPassengerDTO> passengers;
}
