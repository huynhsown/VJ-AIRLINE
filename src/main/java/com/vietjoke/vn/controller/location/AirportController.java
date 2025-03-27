package com.vietjoke.vn.controller.location;

import com.vietjoke.vn.dto.location.AirportDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.location.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/airports")
    @Operation(
            summary = "Retrieve a list of airports",
            description = "This API returns a complete list of airports along with their details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of airports"),
                    @ApiResponse(responseCode = "404", description = "No airports found")
            },
            tags = {"Airports"}
    )
    public ResponseEntity<?> getAllAirports(){
        ResponseDTO<List<AirportDTO>> responseDTO = airportService.getAllAirports();
        return ResponseEntity.ok(responseDTO);
    }

}
