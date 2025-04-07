package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.request.pricing.SeatReservationRequestDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.flight.FlightService;
import com.vietjoke.vn.service.pricing.SeatRedisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SeatController {

    private final SeatRedisService seatRedisService;

    @PostMapping("/booking/reserve-seat")
    public ResponseEntity<?> reserveSeat(@RequestBody @Valid SeatReservationRequestDTO seatRequest) {
        return ResponseEntity.ok(seatRedisService.reserveSeat(seatRequest));
    }

}
