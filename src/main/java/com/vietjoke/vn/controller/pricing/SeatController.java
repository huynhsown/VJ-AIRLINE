package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.dto.request.pricing.SeatReservationRequestDTO;
import com.vietjoke.vn.service.pricing.SeatRedisService;
import com.vietjoke.vn.service.pricing.SeatReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SeatController {

    private final SeatRedisService seatRedisService;
    private final SeatReservationService seatReservationService;

    @GetMapping("/booking/seat-selection")
    @Operation(
            summary = "Check seat selection availability",
            description = "Returns whether the fare class of the selected flight allows seat selection."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns seat selection availability status"),
            @ApiResponse(responseCode = "404", description = "Flight not found or invalid booking session"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getSeatSelectionStatus(@RequestParam String sessionToken,
                                                    @RequestParam String flightNumber) {
        return ResponseEntity.ok(seatReservationService.checkFareClassSeatSelection(sessionToken, flightNumber));
    }

    @PostMapping("/booking/reserve-seat")
    @Operation(
            summary = "Reserve a seat on the selected flight",
            description = "Reserves a seat for the current booking session if seat selection is allowed for the fare class."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat reserved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or seat selection not allowed"),
            @ApiResponse(responseCode = "409", description = "Seat already reserved"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> reserveSeat(@RequestBody @Valid SeatReservationRequestDTO seatRequest) {
        return ResponseEntity.ok(seatRedisService.reserveSeat(seatRequest));
    }

    @DeleteMapping("/booking/release-seat")
    @Operation(
            summary = "Release a reserved seat",
            description = "Releases a previously reserved seat in the current booking session."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat released successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or session not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> releaseSeat(@RequestBody @Valid SeatReservationRequestDTO seatRequest) {
        return ResponseEntity.ok(seatRedisService.releaseSeat(seatRequest));
    }
}
