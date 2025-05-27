package com.vietjoke.vn.controller.user;

import com.vietjoke.vn.dto.booking.PassengersInfoParamDTO;
import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.dto.response.user.PassengerInfoResponseDTO;
import com.vietjoke.vn.service.user.PassengerService;
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
public class PassengerController {

    private final PassengerService passengerService;

    @Operation(
            summary = "Input passenger information",
            description = "Accept passenger information (adult, child, infant) and update booking session accordingly."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Passenger information processed successfully",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or passenger count mismatch",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
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
    @PostMapping("/booking/passenger-info")
    public ResponseEntity<?> inputPassengerInfo(@Valid @RequestBody PassengersInfoParamDTO infoParamDTO){
        return ResponseEntity.ok(passengerService.inputPassengerInfo(infoParamDTO));
    }

    @Operation(
            summary = "Get passenger info in session",
            description = "Retrieve passenger information stored in the booking session using the session token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Passenger info retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PassengerInfoResponseDTO.class))
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
    @GetMapping("/booking/passenger-info")
    public ResponseEntity<?> getPassengerInfo(@RequestParam String sessionToken){
        return ResponseEntity.ok(passengerService.getPassengerInfo(sessionToken));
    }

}
