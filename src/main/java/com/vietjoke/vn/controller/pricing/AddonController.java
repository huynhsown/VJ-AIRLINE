package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.dto.request.pricing.AddonBookingRequestDTO;
import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.util.enums.pricing.AddonStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AddonController {

    private final AddonService addonService;

    @Operation(
            summary = "Get flight addons in session",
            description = "Returns a list of available addons for a specific flight within the current session, with filtering, sorting, and pagination options."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved addons list",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)) // đổi thành DTO tương ứng
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/booking/addons")
    public ResponseEntity<?> getAddonsForFlightInSession(@RequestParam String addonCode,
                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String sortOrder,
                                                    @RequestParam(defaultValue = "1") int pageNumber,
                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                    @RequestParam(defaultValue = "ACTIVE") AddonStatus status,
                                                    @RequestParam String sessionToken,
                                                    @RequestParam String flightNumber) {
        return ResponseEntity.ok(addonService.getAddonsForFlight(addonCode,
                sortBy,
                sortOrder,
                pageNumber,
                pageSize,
                status,
                sessionToken,
                flightNumber));
    }

    @Operation(
            summary = "Book addons for a flight within a booking session",
            description = "Allows user to book or update addons for a specific passenger and flight in the current booking session."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Addons booked/updated successfully",
                    content = @Content(schema = @Schema(implementation = Map.class)) // Trả về map với sessionToken, currentStep, nextStep
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid addon quantity or bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user not authenticated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking session not found or expired",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "412",
                    description = "Missing or invalid booking step",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/booking/addons")
    public ResponseEntity<?> bookAddons(@RequestBody @Valid AddonBookingRequestDTO request) {
        return ResponseEntity.ok(addonService.bookAddons(request));
    }

    @Operation(
            summary = "Get addons selected by a passenger for a specific flight in a session",
            description = "Retrieve the list of addons that a passenger has selected for a particular flight within the current booking session."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved passenger addons",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session expired or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "412",
                    description = "Missing or invalid booking step",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/booking/passengers/{passengerUuid}/flights/{flightNumber}/addons")
    public ResponseEntity<?> getPassengerAddons(
            @PathVariable String passengerUuid,
            @PathVariable String flightNumber,
            @RequestParam String sessionToken){
        return ResponseEntity.ok(addonService.getPassengerAddons(sessionToken, flightNumber, passengerUuid));
    }

}
