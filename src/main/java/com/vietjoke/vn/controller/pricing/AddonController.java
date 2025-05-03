package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.dto.request.pricing.AddonBookingRequestDTO;
import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.util.enums.pricing.AddonStatus;
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
public class AddonController {

    private final AddonService addonService;

    @Operation(
            summary = "Get flight addons in session",
            description = "Returns a list of available addons for a specific flight within the current session, with filtering, sorting, and pagination options."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved addons list"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
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

    @PostMapping("/booking/addons")
    public ResponseEntity<?> bookAddons(@RequestBody @Valid AddonBookingRequestDTO request) {
        return ResponseEntity.ok(addonService.bookAddons(request));
    }

    @GetMapping("/booking/passengers/{passengerUuid}/flights/{flightNumber}/addons")
    public ResponseEntity<?> getPassengerAddons(
            @PathVariable String passengerUuid,
            @PathVariable String flightNumber,
            @RequestParam String sessionToken){
        return ResponseEntity.ok(addonService.getPassengerAddons(sessionToken, flightNumber, passengerUuid));
    }

}
