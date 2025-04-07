package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.request.pricing.SeatReservationRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;

public interface SeatRedisService {
    ResponseDTO<?> reserveSeat(SeatReservationRequestDTO seatRequest);
    ResponseDTO<?> releaseSeat(SeatReservationRequestDTO seatRequest);
    String getSeatLockStatus(SeatReservationRequestDTO seatRequest);
}
