package com.vietjoke.vn.controller.flight;

import com.vietjoke.vn.dto.booking.SearchParamDTO;
import com.vietjoke.vn.dto.request.flight.SelectFlightRequestDTO;
import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.dto.response.flight.SearchFlightResponseDTO;
import com.vietjoke.vn.service.flight.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FlightController {

    private final FlightService flightService;

    @Operation(
            summary = "Search flights",
            description = "Search available flights based on trip parameters such as origin, destination, dates, and passenger count."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Flight search successful",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Flight not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/flight/search")
    public ResponseEntity<?> searchFlight(@Valid @RequestBody SearchParamDTO searchParamDTO) {
        ResponseDTO<SearchFlightResponseDTO> result = flightService.searchFlight(searchParamDTO);
        if(result.getStatus() == HttpStatus.NOT_FOUND.value()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Select flights",
            description = "Allows the user to select flights for the booking session, validating availability and session state."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Flight selection successful",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session expired or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "412",
                    description = "Invalid trip selection or missing booking step",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/flight/select")
    public ResponseEntity<?> selectFlight(@Valid @RequestBody SelectFlightRequestDTO selection){
        return ResponseEntity.ok(flightService.selectFlight(selection));
    }


    @Operation(
            summary = "Get seats by session token",
            description = "Retrieve available seats for the selected flights in the booking session."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seats retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session expired or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "412",
                    description = "Booking step missing or invalid trip selection",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/booking/get-seats")
    public ResponseEntity<?> getSeatsByToken(@RequestParam String sessionToken){
        return ResponseEntity.ok(flightService.getSeatOfFlight(sessionToken));
    }
}
