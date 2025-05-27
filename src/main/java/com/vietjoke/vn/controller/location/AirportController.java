package com.vietjoke.vn.controller.location;

import com.vietjoke.vn.dto.location.AirportDTO;
import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.location.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get all airports",
            description = "Retrieve a list of all airports available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of airports retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No airports found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/airports")
    public ResponseEntity<?> getAllAirports(){
        ResponseDTO<List<AirportDTO>> responseDTO = airportService.getAllAirports();
        return ResponseEntity.ok(responseDTO);
    }

}
