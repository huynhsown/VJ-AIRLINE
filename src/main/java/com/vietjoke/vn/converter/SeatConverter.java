package com.vietjoke.vn.converter;

import com.vietjoke.vn.dto.response.pricing.SeatResponseDTO;
import com.vietjoke.vn.entity.pricing.SeatReservationEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatConverter {

    private final ModelMapper modelMapper;

    public SeatResponseDTO toSeatResponseDTO(SeatReservationEntity seatReservationEntity) {
        return modelMapper.map(seatReservationEntity, SeatResponseDTO.class);
    }

}
