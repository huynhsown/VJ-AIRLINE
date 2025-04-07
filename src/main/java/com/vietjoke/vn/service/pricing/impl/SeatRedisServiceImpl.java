package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.dto.request.pricing.SeatReservationRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.exception.pricing.SeatAlreadyReservedException;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.pricing.SeatRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeatRedisServiceImpl implements SeatRedisService {

    private final BookingSessionService bookingSessionService;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseDTO<?> reserveSeat(SeatReservationRequestDTO seatRequest) {
        BookingSession session = bookingSessionService.getSession(seatRequest.getSessionToken());
        String seatLockKey = buildSeatLockKey(seatRequest);

        Boolean isSaved = stringRedisTemplate.opsForValue()
                .setIfAbsent(seatLockKey, session.getSessionId(), session.getTimeToLive(), TimeUnit.SECONDS);

        if (isSaved == null) {
            throw new RuntimeException("Failed to lock seat: Redis operation returned null");
        }

        if (!isSaved) {
            throw new SeatAlreadyReservedException("Seat " + seatRequest.getSeatNumber() + " is already reserved");
        }

        session = bookingSessionService.addLocketSeat(session.getSessionId(), seatLockKey);
        return ResponseDTO.success(Map.of("sessionToken", session.getSessionId()));
    }

    @Override
    public ResponseDTO<?> releaseSeat(SeatReservationRequestDTO seatRequest) {
        BookingSession session = bookingSessionService.getSession(seatRequest.getSessionToken());
        String seatLockKey = buildSeatLockKey(seatRequest);

        stringRedisTemplate.delete(seatLockKey);
        session = bookingSessionService.removeLocketSeat(session.getSessionId(), seatLockKey);

        return ResponseDTO.success(Map.of("sessionToken", session.getSessionId()));
    }

    @Override
    public String getSeatLockStatus(SeatReservationRequestDTO seatRequest) {
        String seatLockKey = buildSeatLockKey(seatRequest);
        return stringRedisTemplate.opsForValue().get(seatLockKey);
    }

    private String buildSeatLockKey(SeatReservationRequestDTO seatRequest) {
        return "seat:" + seatRequest.getFlightNumber() +
                ":" + seatRequest.getSeatNumber() +
                ":" + seatRequest.getPassengerUUID();
    }
}