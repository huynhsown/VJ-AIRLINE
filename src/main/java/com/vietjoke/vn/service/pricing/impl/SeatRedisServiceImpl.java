package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.dto.request.flight.SelectFlightDTO;
import com.vietjoke.vn.dto.request.pricing.SeatReservationRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.booking.BookingSession;
import com.vietjoke.vn.entity.pricing.FareClassEntity;
import com.vietjoke.vn.exception.flight.FlightNotFoundException;
import com.vietjoke.vn.exception.pricing.SeatAlreadyReservedException;
import com.vietjoke.vn.exception.pricing.SeatSelectionNotAllowedException;
import com.vietjoke.vn.service.booking.BookingSessionService;
import com.vietjoke.vn.service.helper.BookingSessionHelper;
import com.vietjoke.vn.service.pricing.FareClassService;
import com.vietjoke.vn.service.pricing.SeatRedisService;
import com.vietjoke.vn.service.pricing.SeatReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeatRedisServiceImpl implements SeatRedisService {

    private final BookingSessionService bookingSessionService;
    private final StringRedisTemplate stringRedisTemplate;
    private final FareClassService fareClassService;
    private final SeatReservationService seatReservationService;

    @Override
    public ResponseDTO<?> reserveSeat(SeatReservationRequestDTO seatRequest) {
        BookingSession session = bookingSessionService.getSession(seatRequest.getSessionToken());

        boolean seatSelection = Boolean.parseBoolean(
                seatReservationService.checkFareClassSeatSelection(
                        seatRequest.getSessionToken(),
                                seatRequest.getFlightNumber()
                        )
                .getData()
                .get("seatSelectionAllowed"));

        boolean isSeatAvailable = seatReservationService.isSeatServiced(
                seatRequest.getSessionToken(),
                seatRequest.getFlightNumber(),
                seatRequest.getSeatNumber());

        if(!seatSelection || isSeatAvailable) {
            throw new SeatSelectionNotAllowedException("Seat selection not allowed");
        }

        String seatLockKey = buildSeatLockKey(seatRequest);

        Boolean isSaved = stringRedisTemplate.opsForValue()
                .setIfAbsent(seatLockKey, seatRequest.getPassengerUUID(), session.getTimeToLive(), TimeUnit.SECONDS);

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
                ":" + seatRequest.getSeatNumber();
    }
}