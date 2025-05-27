package com.vietjoke.vn.controller.location;

import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.service.location.CountryService;
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

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @Operation(
            summary = "Get all countries",
            description = "Retrieve a list of all countries."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of countries retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
            )
    })
    @GetMapping("/countries")
    public ResponseEntity<?> getAllCountries() {
        return ResponseEntity.ok().body(countryService.getCountries());
    }
}
