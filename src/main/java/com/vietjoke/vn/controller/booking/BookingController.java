package com.vietjoke.vn.controller.booking;

import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.booking.BookingService;
import com.vietjoke.vn.service.booking.BookingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookingController {

    private final BookingSessionService bookingSessionService;
    private final BookingService bookingService;

    @Operation(
            summary = "Get flight booking session information",
            description = "Returns the current booking session information including selected flight details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved booking session information",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session expired or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/booking/flight-info")
    public ResponseEntity<?> getBookingFlightInfo(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.getBookingSessionInfo(sessionToken));
    }

    @Operation(
            summary = "Complete service selection step in the booking process",
            description = "Validates and updates the session step after all required service selections are completed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully completed service selection",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "428",
                    description = "Missing or incorrect booking step",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session expired or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/booking/service/complete")
    public ResponseEntity<?> completeServiceSelection(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.completeServiceSelection(sessionToken));
    }

    @Operation(
            summary = "Get booking preview information",
            description = "Returns a summary of the booking including passenger details, flight summaries, total price, and applied promotions."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved booking preview",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session expired or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "428",
                    description = "Missing or incorrect booking step for preview",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/booking/preview")
    public ResponseEntity<?> getBookingPreview(@RequestParam String sessionToken) {
        return ResponseEntity.ok(bookingSessionService.getBookingPreview(sessionToken));
    }

    @Operation(
            summary = "Get user's booking history",
            description = "Retrieves the list of past bookings for the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved booking history",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/booking/history")
    public ResponseEntity<?> getBookingHistory(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(bookingService.getBookingHistory(username));
    }

    @Operation(
            summary = "Get detailed booking information",
            description = "Returns complete booking details for the authenticated user based on the booking reference."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved booking detail",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User or booking not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/booking/detail")
    public ResponseEntity<?> getBookingDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String bookingReference) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(bookingService.getBookingDetail(username, bookingReference));
    }

    @Operation(
            summary = "Cancel a booking",
            description = "Cancels a booking for the authenticated user identified by the booking reference."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking cancelled successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Flight not found or conflict occurred during cancellation",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/booking/cancel")
    public ResponseEntity<?> cancelBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String bookingReference) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(bookingService.cancelBooking(username, bookingReference));
    }
}
